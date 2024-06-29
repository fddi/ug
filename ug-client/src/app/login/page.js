"use client"

import React, { useEffect, useState } from 'react';
import './login.css'
import { useRouter } from 'next/navigation'
import { Layout, Row, Col, Tabs, App, } from 'antd'
import { lag } from '@/config/lag';
import LoginNotice from './components/LoginNotice';
import LoginByAccount from './components/LoginByAccount';
import LoginByQRCode from './components/LoginByQRCode';
import { getOv, post } from '@/config/client';
export default function Login(props) {
    const [jump, setJump] = useState(false);
    const [appName, setAppName] = useState('');
    const router = useRouter();

    useEffect(() => {
        post('ov/one-public', { unitCode: "Y", optionCode: 'admin_app_name' }).then(result => {
            if (200 === result.resultCode) {
                setAppName(result.resultData)
            }
        })
    }, [])

    useEffect(() => {
        if (jump) {
            router.replace("/home");
        }
    }, [jump])

    function onFinish(logined, result) {
        if (logined) {
            setJump(true)
        }
    }

    return (
        <App>
            <Layout className="content-wrapper">
                <Row className='login-main'>
                    <Col xs={0} sm={0} md={12}
                        className='left-info' >
                        <h2 className="slogan">
                            欢迎使用<br />{appName}
                        </h2>
                        <div className='notice'>
                            <LoginNotice />
                        </div>
                    </Col>
                    <Col className="form">
                        <Tabs defaultActiveKey="1" centered
                            items={[{
                                key: '1',
                                label: lag.loginByAccount,
                                children: <LoginByAccount onFinish={onFinish} />,
                            },
                                // {
                                //     key: '2',
                                //     label: lag.loginByScan,
                                //     children: <LoginByQRCode onFinish={onFinish} />,
                                // }
                            ]}
                        >
                        </Tabs>

                    </Col>
                </Row>
            </Layout>
        </App>
    )
};