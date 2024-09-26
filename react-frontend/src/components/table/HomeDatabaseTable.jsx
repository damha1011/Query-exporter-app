import { Table } from "antd";
import RenderTextInTable from "./RenderTextInTable";

const HomeDatabaseTable = (props) => {
    const { data } = props;

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.name} />
        },
        {
            title: 'Link',
            dataIndex: 'link',
            width: 60,
            render: (_, record) => <RenderTextInTable data={record.link} />
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

export default HomeDatabaseTable;