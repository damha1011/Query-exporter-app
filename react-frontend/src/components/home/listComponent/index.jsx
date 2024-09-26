import { Collapse } from "antd";
import React from "react";
import HomeDatabaseTable from "../../table/HomeDatabaseTable";
import HomeQueryTable from "../../table/HomeQueryTable";
import HomeMetricTable from "../../table/HomeMetricTable";

const ListComponent = (props) => {
    const { state } = props;

    const items = [
        {
            key: '1',
            label: 'Databases',
            children: <HomeDatabaseTable data={state?.data?.databases}/>,
        },
        {
            key: '2',
            label: 'Queries',
            children: <HomeQueryTable data={state?.data?.queries}/>,
        },
        {
            key: '3',
            label: 'Metrics',
            children: <HomeMetricTable data={state?.data?.metrics}/>,
        },
    ];

    return (
        <Collapse items={items} className="mb-4"/>
    );
}

export default ListComponent;