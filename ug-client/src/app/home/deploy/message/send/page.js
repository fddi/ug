"use client"

import React, { Fragment, useEffect, useState } from 'react';
import { Button, Badge, Drawer } from 'antd';
import { BiPaperPlane } from "react-icons/bi";
import DynamicCurd from '@/components/dynamic/DynamicCurd'
import MultiMessageRecord from './components/MultiMessageRecord';

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
                (<Button.Group><Button shape='circle' icon={<BiPaperPlane />} onClick={() => control(record)} /></Button.Group>),
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
            width: 180,
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
    const [modules, setModules] = useState();
    const [multiMessage, setMessage] = useState();
    const [open, setOpen] = useState(false)

    const control = (multiMessage) => {
        setMessage(multiMessage)
        setOpen(true);
    }

    useEffect(() => {
        setModules(getModules(control))
    }, [])

    return (
        <Fragment>
            <DynamicCurd modules={modules} handleSelect={(item) => setMessage(item)} />
            <Drawer title={`【${multiMessage && multiMessage.title}】发送记录`} destroyOnClose={true} width={'calc(48vw)'} open={open} onClose={() => setOpen(false)}>
                <MultiMessageRecord multiMessage={multiMessage} />
            </Drawer>
        </Fragment>
    );
}