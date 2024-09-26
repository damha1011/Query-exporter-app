import { Avatar, Button, Dropdown, Flex, Layout, Space, Typography } from "antd";
import React, { useState } from "react";
import {
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    UserOutlined
} from '@ant-design/icons';
import { dropDownItems } from '../../const';
import { useHistory } from "react-router-dom";
import AccountSetting from "./AccountSetting";

const CustomHeader = (props) => {
    const { collapsed, setCollapsed, colorBgContainer } = props;
    const { Header } = Layout;
    const history = useHistory();

    const [modalOpen, setModalOpen] = useState(false);
    const user = JSON.parse(localStorage.getItem('user'));

    const handleClickDropdownItem = (item) => {
        if (item.key === "1") {
            localStorage.removeItem('token');
            history.push("/login");
        } else if (item.key === "2") {
            setModalOpen(true);
        }
    }

    return (
        <Header
            style={{
                padding: 0,
                background: colorBgContainer,
            }}
        >
            <AccountSetting 
                open={modalOpen} 
                setOpen={setModalOpen}
                user={user}    
            />
            <Flex justify="space-between" align="center" style={{marginRight: '15px'}}>
                <Button
                    type="text"
                    icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                    onClick={() => setCollapsed(!collapsed)}
                    style={{
                        fontSize: '16px',
                        width: 64,
                        height: 64, 
                    }}
                />
                <Space>
                    <Typography.Text>{user?.username}</Typography.Text>
                    <Dropdown
                        menu={{
                            items: dropDownItems,
                            selectable: true,
                            onClick: handleClickDropdownItem,
                        }}
                    >
                        <Avatar size="large" icon={<UserOutlined />}/>
                    </Dropdown>
                </Space>
            </Flex>
        </Header>
    )
}

export default CustomHeader;