"use client"

import React, { useState, useEffect, useContext } from 'react';
import { App, Card, Row, Col, Space, Upload, Avatar, Typography, Button } from 'antd';
import ImgCrop from "antd-img-crop";
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons'
import { APIURL, APPID, TAG, exportNextPath, getAuthInfo, getImgUrl, post } from '@/config/client';
import { HomeContext } from '@/app/home/components/HomeContext';
import { useRouter } from 'next/navigation'

export default function AccountLayout({ children }) {
    const [loading, setLoading] = useState(false)
    const [imgUrl, setImgUrl] = useState()
    const [account, setAccount] = useState({})
    const [nickName, setNickName] = useState('')
    const { setActiveMenu } = useContext(HomeContext);
    const router = useRouter();
    const { message } = App.useApp();

    useEffect(() => {
        setAccount(getAuthInfo())
        setNickName(getAuthInfo().nickName)
        setImgUrl(`${APIURL}/user/file/avatar/${getAuthInfo().userName}?v=${new Date().getTime()}`)
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
            setLoading(false)
            setImgUrl(`${APIURL}/user/file/avatar/${account.userName}?v=${new Date().getTime()}`)
        }
    };

    function onAccountInfo() {
        router.push(exportNextPath(`/home/account_information`))
        setActiveMenu && setActiveMenu({ label: '个人信息' })
    }

    function onMessageCenter() {
        router.push(exportNextPath(`/home/account_information/notifications`))
        setActiveMenu && setActiveMenu({ label: '消息中心' })
    }

    const uploadButton = (
        <div>
            {loading ? <LoadingOutlined /> : <PlusOutlined />}
            <div className="ant-upload-text">上传头像</div>
        </div>
    );

    function onNickNameChange(v) {
        setNickName(v)
        post("user/update-nickname", {
            nickname: v,
        });
        account.nickName = v;
        sessionStorage.setItem(TAG.token, JSON.stringify(account));
    }

    return (
        <Row gutter={8} style={{ padding: 5, }}>
            <Col span={5}>
                <Card styles={{
                    body: {
                        display: 'flex',
                        flexDirection: 'column', alignItems: 'center', alignContent: 'center'
                    }
                }}
                    actions={[<Button type='text'
                        onClick={onAccountInfo} key="a-1"
                    >个人信息</Button>, <Button type='text' key="a-2"
                        onClick={onMessageCenter}>消息中心</Button>
                    ]}
                >
                    <Space direction='vertical'>
                        <ImgCrop>
                            <Upload
                                name="avatar"
                                listType="picture-circle"
                                className="avatar-uploader c-avatar"
                                showUploadList={false}
                                beforeUpload={beforeUpload}
                                onChange={handleChange}
                                action={`${APIURL}/user/file/upload-avatar`}
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
                            <Typography.Text
                                editable={{
                                    onChange: onNickNameChange,
                                }}>
                                {nickName}
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