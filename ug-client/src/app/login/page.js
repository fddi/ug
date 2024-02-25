"use client"

import React, { useEffect, useState } from 'react';
import './login.css'
import { useRouter } from 'next/navigation'
import { Layout, Row, Col, Tabs, } from 'antd'
import { APPNMAE } from '@/config/client'
import { lag } from '@/config/lag';
import LoginNotice from './components/LoginNotice';
import LoginByAccount from './components/LoginByAccount';
import LoginByQRCode from './components/LoginByQRCode';


export default function Login(props) {
    const [jump, setJump] = useState(false);
    const router = useRouter();
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
        <Layout className="content-wrapper">
            <Row className='login-main'>
                <Col xs={0} sm={0} md={12}
                    className='left-info' >
                    <h2 className="slogan">
                        欢迎使用<br />{APPNMAE}
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
    )
};