import React from 'react';
import { useHistory } from 'react-router-dom';
import { useAuth } from '../../AuthContext';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Divider, Form, Input, Typography, notification } from 'antd';
import * as AuthService from '../../services/AuthService';
import ImageBackground from '../../images/login-background.jpg';

const LoginPage = () => {
  const { login } = useAuth();
  const history = useHistory();
  const [api, contextHolder] = notification.useNotification();

  const onFinish = async (value) => {
    try {
      const response = await AuthService.login(value);
      if (response.status === 200) {
        const token = response?.data?.accessToken; // Assuming your API returns a 'token' property in the response
        login(token); // storage token into local storage
        localStorage.setItem('user', JSON.stringify(response?.data?.user));
        history.push('/home');
      }
    } catch (err) {
      api["error"]({
        message: "Login failed",
        description: err?.response?.data?.message,
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
          overflow: 'hidden'
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
        maxWidth: '30%'
      }}>
        <Typography.Title style={{fontSize: '2rem', margin: '1.5rem 0'}}>
            Login
        </Typography.Title>
        <Typography.Text style={{fontSize: '1rem', fontWeight: 'bold'}}>
            Login to your account
        </Typography.Text>
        <div>
          <Typography.Text type='secondary'>
              Thank you gor get back to our website, lets access to manage and download your configuration file!
          </Typography.Text>
        </div>
          <Divider />
          <Form
            layout='vertical'
            name="normal_login"
            style={{
              maxWidth: '80%',    
            }}
            initialValues={{
              remember: true,
            }}
            onFinish={onFinish}
          >
            <Form.Item
              label="Username"
              name="username"
              rules={[
                  {
                  required: true,
                  message: 'Username in required!',
                  },
              ]}
            >
              <Input prefix={<UserOutlined />} placeholder="Username" />
            </Form.Item>
            <Form.Item
              label="Password"
              name="password"
              rules={[
                  {
                  required: true,
                  message: 'Password in required!',
                  },
              ]}
            >
              <Input.Password
                  prefix={<LockOutlined />}
                  type="password"
                  placeholder="Password"
              />
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                style={{
                    width: '100%',
                    height: '2.3rem',
                    marginBottom: '2rem'
                }}
              >
                Sign in
              </Button>
              <Typography.Text style={{fontSize: '1rem'}}>
                {`Don't have an account yet? `}
              </Typography.Text>
              <Typography.Link
                style={{ fontSize: '1rem' }}
                href='/register'
              >
                Join us
              </Typography.Link>
            </Form.Item>
          </Form>
        </div>
    </div>
  );
}

export default LoginPage;
