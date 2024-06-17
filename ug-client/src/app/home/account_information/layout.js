"use client"

import React, { useState, useRef, useEffect } from 'react';
import { Card, Row, Col, Space, Upload, Avatar, Typography } from 'antd';
import ImgCrop from "antd-img-crop";
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons'
import { APPID, getAuthInfo, getImgUrl } from '@/config/client';

export default function AccountLayout({ children }) {
    const [loading, setLoading] = useState(false)
    const [imgUrl, setImgUrl] = useState()
    const [account, setAccount] = useState({})
    useEffect(() => {
        setAccount(getAuthInfo())
    }, [])

    const beforeUpload = (file) => {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('只限jpg或png格式的图片!');
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
            message.error('图片大小必须在2MB以内!');
        }
        return isJpgOrPng && isLt2M;
    }

    const handleChange = info => {
        if (info.file.status === 'uploading') {
            setLoading(true)
            return;
        }
        if (info.file.status === 'done') {
            const result = info.file.response;
            setLoading(false)
            setImgUrl(getImgUrl(result.resultData))
        }
    };

    const uploadButton = (
        <div>
            {loading ? <LoadingOutlined /> : <PlusOutlined />}
            <div className="ant-upload-text">上传头像</div>
        </div>
    );
    return (
        <Row gutter={8} style={{ padding: 5, }}>
            <Col span={5}>
                <Card styles={{
                    body: {
                        display: 'flex',
                        flexDirection: 'column', alignItems: 'center'
                    }
                }}>
                    <Space direction='vertical'>
                        <ImgCrop>
                            <Upload
                                name="logo"
                                listType="picture-circle"
                                className="avatar-uploader c-avatar"
                                showUploadList={false}
                                beforeUpload={beforeUpload}
                                onChange={handleChange}
                                action={``}
                                headers={{
                                    'Accept': 'application/json',
                                    'appId': APPID,
                                    'token': account.token || ''
                                }}
                            >
                                {imgUrl ? <Avatar size={90} src={imgUrl} /> : uploadButton}
                            </Upload>
                        </ImgCrop>
                        <Typography style={{ width: '100%', }}>
                            <Typography.Text>
                                {account.nickName || ''}
                            </Typography.Text>
                        </Typography>
                    </Space>
                </Card>
            </Col>
            <Col span={19}>
                <Card>
                    {children}
                </Card>
            </Col>
        </Row>
    )
}