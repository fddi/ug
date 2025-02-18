"use client"

import React, { useState, useRef, useEffect } from 'react';
import { Button, Row, Form, Input, Spin, Alert, Checkbox, Modal, Divider, Space, } from 'antd'
import { TAG, post } from '@/config/client'
import { lag } from '@/config/lag';
import StringUtils from '@/util/StringUtils'
import ModifyPwd from './ModifyPwd';
import AES from '@/util/AES'

const FormItem = Form.Item;

export default function LoginByAccount(props) {
    const [loading, setLoading] = useState(false)
    const [userName, setUserName] = useState();
    const [rememberUser, setRememberUser] = useState();
    const [visible, setVisible] = useState(false);
    const [errorMsg, setErrorMsg] = useState();
    const [form] = Form.useForm();
    useEffect(() => {
        let lastRememberUser = true;
        let lastUserName = localStorage.getItem(TAG.userName)
        if (StringUtils.isEmpty(lastUserName)) {
            lastUserName = ""
            lastRememberUser = false
        }
        setUserName(lastUserName);
        setRememberUser(lastRememberUser)
        form.setFieldsValue({ userName: lastUserName })
    }, [])
    const pwRef = useRef(null);

    function onFinish(values) {
        setLoading(true)
        const encStr = AES.encrypt(values.password);
        values.password = encStr;
        post('auth/login', values).then(result => {
            if (result && 200 === result.resultCode) {
                let tokenInfo = result.resultData;
                sessionStorage.setItem(TAG.token, JSON.stringify(tokenInfo));
                if (rememberUser) {
                    localStorage.setItem(TAG.userName, tokenInfo.userName);
                } else {
                    localStorage.removeItem(TAG.userName)
                }
                setLoading(false);
                setErrorMsg(null)
                props.onFinish && props.onFinish(true, result);
            } else if (result) {
                setLoading(false);
                setErrorMsg(result.resultMsg)
                props.onFinish && props.onFinish(false, result);
            } else {
                setLoading(false);
                setErrorMsg(lag.errorNetwork)
                props.onFinish && props.onFinish(false, result);
            }
        })
    }

    function handlePressEnter(e) {
        e.preventDefault()
        pwRef.current.focus();
        pwRef.current.select();
    }
    const loginAlert = errorMsg && (<Alert banner message={errorMsg} type="error" showIcon />);
    return (
        <Spin spinning={loading} tip={lag.infoLoading}>
            <Form onFinish={onFinish} style={{ padding: 30 }} form={form}>
                <FormItem hasFeedback name="userName"
                    rules={[
                        {
                            required: true,
                            pattern: /^[A-Za-z0-9]{4,16}/,
                            message: lag.alertRequireUsername
                        },
                    ]}>
                    <Input autoFocus size="large" placeholder="用户名：developer" onPressEnter={handlePressEnter} />
                </FormItem>
                <FormItem hasFeedback name="password"
                    rules={[
                        {
                            required: true,
                            message: lag.alertRequirePwd
                        },
                    ]}>
                    <Input ref={pwRef} size="large" type="password" placeholder="密码：Ulug@2024" />
                </FormItem>
                <Row style={{ padding: 10 }}>
                    <Checkbox checked={rememberUser}
                        onChange={(e) => { setRememberUser(e.target.checked) }}>{lag.rememberUsername}</Checkbox>
                </Row>
                {loginAlert}
                <Row>
                    <Button type="primary" shape="round" size="large" htmlType="submit" style={{ width: "100%" }} >
                        {lag.login}
                    </Button>
                </Row>
                <Space style={{ margin: 10, }}>
                    <Button type="link" size='small'
                        onClick={() => { setVisible(true) }}>
                        {lag.changePassword}
                    </Button>
                    <Divider type="vertical" />
                    <Button type="link" size='small'
                        onClick={() => { }}>
                        {lag.callAdmin}
                    </Button>
                </Space>
            </Form>
            <Modal
                title={lag.changePassword}
                open={visible}
                footer={null}
                onCancel={() => setVisible(false)}
            >
                <ModifyPwd userName={userName}
                    onFinish={() => setVisible(false)} />
            </Modal>
        </Spin>
    )
}