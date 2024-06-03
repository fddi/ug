"use client"

import React, { useEffect, useState } from 'react';
import { post } from '@/config/client';
import { Button, Spin, Badge, Modal, App, Drawer } from 'antd';
import { BiPaperPlane, BiSidebar } from "react-icons/bi";
import DynamicCurd from '@/components/dynamic/DynamicCurd'
import RoleSearch from '@/app/home/auth/role/components/RoleSearch';
import StringUtils from '@/util/StringUtils';
import { lag } from '@/config/lag';

function getModules(control) {
    return {
        rowKey: "messageId",
        queryApi: "multiMessage/page-list",
        saveApi: 'multiMessage/save',
        delApi: 'multiMessage/del',
        searchKey: "title",
        columns: [{
            title: 'ID',
            dataIndex: 'messageId',
            inputType: "hidden",
            colsType: 'hidden'
        },
        {
            title: "操作",
            colsType: "editAndDel",
            width: 80
        }, {
            title: '发送',
            width: 80,
            render: (text, record) =>
            (<Button.Group><Button icon={<BiPaperPlane />} onClick={() => control(record, 1)} />
                <Button icon={<BiSidebar />} onClick={() => control(record, 2)} /></Button.Group>),
            align: 'center',
        }, {
            title: '标题',
            dataIndex: 'title',
            inputType: 'text',
            width: 120,
            required: true
        }, {
            title: '文字消息',
            dataIndex: 'message',
            inputType: 'textArea',
            required: true
        }, {
            title: '服务地址',
            dataIndex: 'actionUrl',
            inputType: 'text',
            width: 120,
        }, {
            title: '等级',
            dataIndex: 'level',
            inputType: 'select',
            catalog: 'notice_level',
            width: 80,
        }, {
            title: '状态',
            dataIndex: 'status',
            width: 60,
            render: (text, record) => {
                if (text == "1") {
                    return (<Badge status="processing" />)
                }
                return (<Badge status="default" />)
            },
            align: 'center',
            inputType: 'select',
            catalog: 'TF',
            required: true
        }],
    }
}

export default function MultiMessageSend(props) {
    const [loading, setLoading] = useState(false)
    const [modules, setModules] = useState();
    const [refreshTime, setRefreshTime] = useState(Date.now());
    const [multiMessage, setMessage] = useState();
    const [visible, setVisible] = useState(false)
    const [open, setOpen] = useState(false)
    const { message } = App.useApp();

    const control = (multiMessage, tag) => {
        setMessage(multiMessage)
        switch (tag) {
            case 1:
                setVisible(true);
                break;
            case 2:
                setOpen(true);
                break;
            default:
                break;
        }
    }

    useEffect(() => {
        setModules(getModules(control))
    }, [])


    const onFinish = (selectKeys) => {
        if (StringUtils.isEmpty(selectKeys)) {
            message.error(lag.noData);
            return;
        }
        setLoading(true)
        post("multiMessage/send",
            {
                ...multiMessage,
                selectKeys: selectKeys.join(','),
            }).then((result) => {
                setLoading(false)
                if (200 === result.resultCode) {
                    setVisible(false)
                    message.info(result.resultMsg)
                }
            });
    }

    return (
        <Spin spinning={loading}>
            <DynamicCurd modules={modules} refreshTime={refreshTime} handleSelect={(item) => setMessage(item)} />
            <Modal
                title={`选择发送【${multiMessage && multiMessage.title}】目标岗位`}
                open={visible}
                footer={null}
                onCancel={() => setVisible(false)}
            >
                <Spin spinning={loading}>
                    <RoleSearch onFinish={onFinish} />
                </Spin>
            </Modal>
            <Drawer title={`【${multiMessage && multiMessage.title}】发送记录`} destroyOnClose={true} width={'calc(48vw)'} open={open} onClose={() => setOpen(false)}>

            </Drawer>
        </Spin>
    );
}