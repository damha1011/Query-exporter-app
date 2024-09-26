import React from 'react';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input, Typography, Divider, notification } from 'antd';
import { register } from '../../services/AuthService';
import { useHistory } from 'react-router-dom';
import ImageBackground from '../../images/login-background.jpg';

const RegisterPage = () => {
    const [api, contextHolder] = notification.useNotification();
    const history = useHistory();

    const onFinish = async (value) => {
        try {
            const response = await register(value);
            if (response.status === 200) {
                history.push("/login");
            } 
        } catch (err) {
            console.log(err);
            api["error"]({
                message: "Registration failed",
                description: err.response.data.message,
            });
        }
    };

    return (
        <div
            style={{
                boxSizing: 'border-box',
                width: '100%',
                height: '100vh',
                display: 'flex',
                overflow: 'hidden',
            }}
        >   
            {contextHolder}
            <div style={{
                boxSizing: 'border-box',
                height: '100%',
            }}>
                <img    
                    alt='background'
                    src={ImageBackground}
                    style={{width:'100%', height: '100%'}}
                />
            </div>
            <div style={{
                margin: '2rem 0 1rem 3rem',
                maxWidth: '30%',
                overflowY: 'scroll'
            }}>
                <Typography.Title style={{fontSize: '2rem', margin: '1.5rem 0'}}>
                    Register
                </Typography.Title>
                <Typography.Text style={{fontSize: '1rem', fontWeight: 'bold'}}>
                    Manage your account
                </Typography.Text>
                <div>
                    <Typography.Text type='secondary'>
                        Let's get you all set up so you can verify your personal account and begin setting up your profile
                    </Typography.Text>
                </div>
                <Divider />
                <Form
                    style={{maxWidth: '80%'}}
                    initialValues={{remember: true}}
                    // form={form}
                    onFinish={onFinish}
                    layout='vertical'
                >
                    <Form.Item
                        label="Username:"
                        name="username"
                        rules={[
                            {
                                required: true,
                                message: 'Username is required!',
                            },
                        ]}
                    >
                        <Input prefix={<UserOutlined />} placeholder="Username" />
                    </Form.Item>
                    <Form.Item
                        label="Password:"
                        name="password"
                        rules={[
                            {
                                required: true,
                                message: 'Password is required!',
                            },
                        ]}
                    >   
                        <Input.Password
                            prefix={<LockOutlined />}
                            type="password"
                            placeholder="Password"
                        />
                    </Form.Item>
                    <Form.Item
                        label="Full name:"
                        name="fullName"
                        rules={[
                            {
                                required: true,
                                message: 'Full name is required!',
                            },
                        ]}
                    >
                        <Input placeholder="Full name" />
                    </Form.Item>
                    <Form.Item
                        label="Email:"
                        name="email"
                        rules={[
                            {
                            required: true,
                            message: 'Email is required!',
                            },
                        ]}
                    >
                        <Input placeholder="Email"/>
                    </Form.Item>
                    <Form.Item
                        label="Phone number:"
                        name="phoneNumber"
                    >
                        <Input placeholder="Phone number"/>
                    </Form.Item>
                    <Form.Item
                        label="Address:"
                        name="address"
                    >
                        <Input placeholder="Address"/>
                    </Form.Item>
                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            style={{
                                width: '100%',
                                height: '2.3rem',
                            }}
                        >
                            Register
                        </Button>
                    </Form.Item>
                </Form>
                <Typography.Text style={{fontSize: '1rem'}}>
                    {`Already have an account? `}
                </Typography.Text>
                <Typography.Link
                    href='/login'
                    style={{ fontSize: '1rem' }}
                >
                    Log in
                </Typography.Link>
            </div>
        </div>
    );
}

export default RegisterPage;
