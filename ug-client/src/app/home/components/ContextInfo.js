"use client"

import React, { useState, useEffect } from 'react';
import { App, Button, Form, Input } from 'antd';
import { TAG, getAuthInfo, post } from '@/config/client';

export default function ContextInfo() {
    const [account, setAccount] = useState({})
    const [form] = Form.useForm();
    const { message } = App.useApp();

    useEffect(() => {
        setAccount(getAuthInfo())
        form.setFieldsValue(getAuthInfo())
    }, [])

    function onFinish(values) {
        post("user/update-context", values).then((result) => {
            if (200 === result.resultCode) {
                message.success(result.resultMsg);
            } else {
                message.error(result.resultMsg);
            }
        })
        account.phoneNumber = values.phoneNumber;
        account.address = values.address;
        sessionStorage.setItem(TAG.token, JSON.stringify(account));
    }

    return (
        <Form form={form}
            name="basic"
            labelCol={{
                span: 8,
            }}
            wrapperCol={{
                span: 16,
            }}
            style={{
                maxWidth: 600,
            }}
            onFinish={onFinish}
            autoComplete="off"
        >
            <Form.Item
                label="用户名"
                name="userName"
            >
                <Input disabled />
            </Form.Item>
            <Form.Item
                label="所属机构"
                name="orgName"
            >
                <Input disabled />
            </Form.Item>
            <Form.Item
                label="电话号码"
                name="phoneNumber"
            >
                <Input />
            </Form.Item>

            <Form.Item
                label="常用地址"
                name="address"
            >
                <Input />
            </Form.Item>
            <Form.Item
                wrapperCol={{
                    offset: 8,
                    span: 16,
                }}
            >
                <Button type="primary" htmlType="submit">
                    保存
                </Button>
            </Form.Item>
        </Form>
    )
}