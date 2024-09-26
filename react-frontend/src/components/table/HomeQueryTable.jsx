import { Table } from "antd";
import RenderTextInTable from "./RenderTextInTable";

const HomeQueryTable = (props) => {
    const { data } = props;

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.name} />
        },
        {
            title: 'Databases',
            dataIndex: 'databases',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.databases} />
        },
        {
            title: 'Metrics',
            dataIndex: 'metrics',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.metrics} />
        },
        {
            title: 'sql',
            dataIndex: 'sql',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.sql} />
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

export default HomeQueryTable;