"use client"

import React, { useEffect, useState } from 'react';
import { post } from '@/config/client';
import { App,Button, Spin, Badge, Popconfirm, } from 'antd';
import { CaretRightOutlined, ThunderboltOutlined, PauseOutlined } from '@ant-design/icons'
import DynamicCurd from '@/components/dynamic/DynamicCurd'

function getModules(control) {
    return {
        pageable: false,
        rowKey: "taskId",
        queryApi: "quartz/dev/list",
        saveApi: 'quartz/dev/build',
        delApi: 'quartz/dev/remove',
        columns: [{
            title: 'ID',
            dataIndex: 'taskId',
            inputType: "hidden",
            colsType: 'hidden'
        },
        {
            title: "操作",
            colsType: "editAndDel",
            width: 120
        }, {
            title: '触发',
            width: 60,
            render: (text, record) =>
            (<Popconfirm
                title="确认，将立刻触发此任务?"
                onConfirm={() => control(record, 1)}
                okText="确认"
                cancelText="取消"
            >
                <Button icon={<ThunderboltOutlined />} />
            </Popconfirm>),
            align: 'center',
        }, {
            title: '切换',
            width: 120,
            render: (text, record) =>
            (<Button.Group><Button icon={<PauseOutlined />} onClick={() => control(record, 2)} />
                <Button icon={<CaretRightOutlined />} onClick={() => control(record, 3)} /></Button.Group>),
            align: 'center',
        }, {
            title: '名称',
            dataIndex: 'taskName',
            inputType: 'text',
            width: 120,
            required: true
        }, {
            title: '服务节点',
            dataIndex: 'taskGroup',
            inputType: 'text',
            width: 120,
            required: true,
            defaultValue: 'ulug-core'
        }, {
            title: '服务类',
            dataIndex: 'taskService',
            inputType: 'text',
            width: 120,
            required: true
        }, {
            title: '表达式',
            dataIndex: 'cronExp',
            inputType: 'text',
            width: 150,
            required: true
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
        }, {
            title: '下一次',
            dataIndex: 'nextDate',
            width: 150,
        }],
    }
}

export default function QuartzMgr(props) {
    const [loading, setLoading] = useState(false)
    const [modules, setModules] = useState();
    const [refreshTime, setRefreshTime] = useState(Date.now());
    const { message } = App.useApp()

    const control = (task, tag) => {
        setLoading(true)
        post("quartz/dev/control", { taskId: task.taskId, tag }).then((result) => {
            setRefreshTime(Date.now())
            setLoading(false)
            if (result.resultCode != '200') {
                message.error(result.resultMsg)
            }
        });
    }

    useEffect(() => {
        setModules(getModules(control))
    }, [])


    return (
        <Spin spinning={loading}>
            <DynamicCurd modules={modules} refreshTime={refreshTime} />
        </Spin>
    );
}