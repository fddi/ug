"use client"

import React, { Fragment, useEffect, useState } from 'react';
import { Button, Spin, Modal, App, } from 'antd';
import AsyncTable from '@/components/AsyncTable';
import RoleSearch from '@/app/home/components/RoleSearch';
import StringUtils from '@/util/StringUtils';
import { lag } from '@/config/lag';
import { post } from '@/config/client';

function getModules(multiMessageId) {
    return {
        rowKey: "recordId",
        queryApi: "multiMessage/page-list-record",
        searchKey: "userName",
        params: { multiMessageId },
        columns: [{
            title: 'ID',
            dataIndex: 'recordId',
            colsType: 'hidden'
        }, {
            title: '送达用户',
            dataIndex: 'userName',
            width: 120
        }, {
            title: '发送时间',
            dataIndex: 'gmtCreate',
            required: true,
            width: 120,
        }, {
            title: '发送状态',
            dataIndex: 'sendStatus',
            width: 80,
            render: (text, record) => {
                if (text == "1") {
                    return "已发送"
                }
                return "异常"
            },
            align: 'center',
        }, {
            title: '读取状态',
            dataIndex: 'readStatus',
            width: 80,
            render: (text, record) => {
                if (text == "1") {
                    return "已读"
                }
                return "未读"
            },
            align: 'center',
        }, {
            title: '读取时间',
            dataIndex: 'readTime',
            required: true,
            width: 120,
        }],
    }
}


export default function MultiMessageRecord(props) {
    const { multiMessage } = props;
    const [loading, setLoading] = useState(false)
    const [modules, setModules] = useState();
    const [refreshTime, setRefreshTime] = useState(Date.now());
    const [visible, setVisible] = useState(false)
    const { message } = App.useApp();
    useEffect(() => {
        setModules(getModules(multiMessage && multiMessage.messageId))
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
                setRefreshTime(Date.now())
                if (200 === result.resultCode) {
                    setVisible(false)
                    message.info(result.resultMsg)
                }
            });
    }

    return (
        <Fragment>
            <Button.Group><Button onClick={() => setVisible(true)} type='primary'>发送消息</Button></Button.Group>
            <AsyncTable refreshTime={refreshTime} modules={modules} scroll={{ y: 'calc(100vh - 260px)' }} />
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
        </Fragment>
    )
}