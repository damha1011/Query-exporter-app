import { Button, Col, Flex, Form, Input, Modal, Radio, Row, notification } from "antd";
import React, { useEffect, useState } from "react";
import DatabaseService from "../../../services/DatabaseService";
import RenderTextInTable from "../../table/RenderTextInTable";
import { convertKeyValueToString, convertStringToKeyValueData, openNotification } from "../../../utils";
import TableComponent from "../../table";
import AddLabelModal from "./AddLabelModal";
import { linkDbRegex } from "../../../const";

const DatabaseModal = (props) => { // databasesState, setDatabasesState
    const [form] = Form.useForm();
    const [database, setDatabase] = useState();
    const [selectedRows, setSelectedRows] = useState([]);
    const [labels, setLabels] = useState([]);
    const [lableModalOpen, setLabelModalOpen] = useState(false);
    const [labelSelected, setLabelSelected] = useState();

    const {editable, databaseSelected, isDatabaseModalOpen, refresh} = props?.databasesState;
    const {databasesState, setDatabasesState} = props;

    const [api, contextHolder] = notification.useNotification();

    const columns = [
        {
            title: 'Key',
            dataIndex: 'key',
            ellipsis: true,
            render: (_, record) => <RenderTextInTable data={record.key}/>
        },
        {
            title: 'Value',
            dataIndex: 'value',
            ellipsis: true,
            render: (_, record) => <RenderTextInTable data={record.value}/>
        },
    ]

    const handleOk = async (value) => {
        try {
            const sendValue = {
                ...value,
                label: convertKeyValueToString(labels)
            };
            if (databaseSelected) {
                await DatabaseService.updateDatabase(sendValue, database?.id, localStorage.getItem('token'));
                openNotification(api, "success", "Succeed", "Database updated successfully!");
            } else {
                await DatabaseService.createDatabase(sendValue, localStorage.getItem('token'));
                openNotification(api, "success", "Succeed", "Database created successfully!");
            }
        } catch (err) {
            openNotification(api, "error", "Failed", err?.response?.data?.message);
        }
    }

    const handleClose = () => {
        setLabels([]);
        setDatabasesState({
            ...databasesState, 
            isDatabaseModalOpen: false,
            databaseSelected: null,
            refresh: !refresh,
        })
        form.resetFields();
    }

    const onChange = (e) => {
        setDatabase({
            ...database,
            [e.target.name]: e.target.value
        })
    }

    const handleDeleteLabel = () => {
        const newLabels = labels.filter(label => !selectedRows.includes(label.id));
        setLabels(newLabels);
        setSelectedRows([]);
    }

    const handleAddLabel = () => {
        setLabelModalOpen(true);
    }

    useEffect(() => {
        try {
            if (databaseSelected) {
                DatabaseService.getDatabaseById(databaseSelected,localStorage.getItem('token')).then( res => {
                    setDatabase({...res.data});
                    form.setFieldsValue({
                        link: res.data?.link,
                        connectSQL: res.data?.connectSQL,
                        serviceCode: res.data?.serviceCode,
                        keepConnect: res.data?.keepConnect,
                        autoCommit: res.data?.autoCommit
                    })
                    setLabels(convertStringToKeyValueData(res.data?.label));
                })
            }
        } catch (error) {
            openNotification(api, "error", "Failed", "Network error!");
        }
    }, [databaseSelected])
    
    return (
        <Modal 
            title={editable
                ? databaseSelected ? "Edit Database" : "Create Database"
                : "View Database"}
            open={isDatabaseModalOpen} 
            onOk={() => {
                form.validateFields()
                  .then(async (value) => {
                    await handleOk(value);    
                    setDatabasesState({...databasesState, loading: true});
                    handleClose();
                });
            }} 
            onCancel={handleClose}
        >
            {contextHolder}
            <Form
                form={form}
                layout="vertical"
            >
                <Row gutter={[16,0]}>
                    <Col span={24}>
                        <Form.Item 
                            label="Link:"
                            name="link"
                            placeholder="Link"
                            rules={[
                                {
                                  required: true,
                                  message: "Link is required!",
                                },
                                () => ({
                                    validator(_, value) {
                                        if (!value) {
                                            return Promise.resolve();
                                        }
                                        if (!linkDbRegex.test(value)) {
                                            return Promise.reject(new Error('Link must be followed format: [db]+[driver]://user:pass@host:port/db_name'));
                                        }
                                        return Promise.resolve();
                                    },
                                }),
                            ]}
                        >
                            <Input value={database?.link} disabled={!editable}/>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item 
                            label="Connect SQL:"
                            name="connectSQL"
                            placeholder="Connect Sql"
                        >
                            <Input value={database?.connectSQL} disabled={!editable}/>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item 
                            label="Service Code:"
                            name="serviceCode"
                            placeholder="Service Code"
                        >
                            <Input value={database?.serviceCode} disabled={!editable}/>
                        </Form.Item>
                    </Col>
                    <Col span={24}>
                        <Form.Item 
                            label="Labels:"
                            name="label"
                        >
                            <AddLabelModal 
                                open={lableModalOpen}
                                setOpen={setLabelModalOpen}
                                labelSelected={labelSelected}
                                setLabelSelected={setLabelSelected}
                                labels={labels}
                                setLabels={setLabels}
                            />
                            <Flex justify="space-between" align="center">
                                <Button 
                                    size="small" 
                                    type="primary" 
                                    className="mb-2"
                                    onClick={handleAddLabel}
                                    disabled={!editable}
                                >
                                    Add label
                                </Button>
                                {selectedRows.length > 0 &&
                                    <Button danger className="mb-2" size="small" onClick={handleDeleteLabel} disabled={!editable}>
                                        Delete
                                    </Button>
                                }
                            </Flex>
                            <TableComponent
                                columns={columns}
                                setSelectedRows={setSelectedRows}
                                labels={labels}
                            />
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item 
                            label="Keep connect:"
                            name="keepConnect"    
                            placeholder="Keep connect"
                        >
                            <Radio.Group onChange={onChange} value={database?.keepConnect} disabled={!editable}>
                                <Radio value={"true"}>True</Radio>
                                <Radio value={"false"}>False</Radio>
                            </Radio.Group>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item 
                            label="Auto commit:"
                            name="autoCommit"
                            placeholder="Auto commit"
                        >
                            <Radio.Group onChange={onChange} value={database?.autoCommit} disabled={!editable}>
                                <Radio value={"true"}>True</Radio>
                                <Radio value={"false"}>False</Radio>
                            </Radio.Group>
                        </Form.Item>
                    </Col>
                </Row>
            </Form>
        </Modal>
    )
}

export default DatabaseModal;