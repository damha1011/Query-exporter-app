import { Table } from "antd";
import RenderTextInTable from "./RenderTextInTable";

const HomeMetricTable = (props) => {
    const { data } = props;

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.name} />
        },
        {
            title: 'Type',
            dataIndex: 'type',
            width: 60,  
            render: (_, record) => <RenderTextInTable data={record.type} />
        },
        {
            title: 'Labels',
            dataIndex: 'labels',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.labels} />
        },
    ]

    return (
        <Table
            rowKey={(record) => record?.id}
            columns={columns}
            dataSource={data}
            bordered
            pagination={false}
        />
    )
}

export default HomeMetricTable;