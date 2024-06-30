"use client"

import React, { useEffect, useState } from 'react';
import { post } from '@/config/client';
import { App, Button, Spin, } from 'antd';
import { DeleteOutlined, } from '@ant-design/icons'
import { lag } from '@/config/lag';
import AsyncTable from '@/components/AsyncTable';


export default function AbilityList(props) {
    const [loading, setLoading] = useState(false)
    const [modules, setModules] = useState()
    const [refreshTime, setRefreshTime] = useState(new Date().getTime())
    const { modal } = App.useApp()

    useEffect(() => {

        const defaultModules = {
            pageable: false,
            rowKey: "abilityId",
            queryApi: "ability/dev/list",
            selectType: 'checkbox',
            searchKey: 'abilityNote',
            columns: [{
                title: 'ID',
                dataIndex: 'abilityId',
                colsType: "hidden",
            }, {
                title: '删除',
                width: 60,
                render: (text, record) =>
                    (<Button size='small' danger type='default' shape="circle" icon={<DeleteOutlined />} onClick={() => onDel(record)} />),
                align: 'center',
            }, {
                title: '接口说明',
                dataIndex: 'abilityNote',
                width: 200,
                sorter: (a, b) => a.abilityNote.localeCompare(b.abilityNote),
            }, {
                title: '接口地址',
                dataIndex: 'abilityUri',
                width: 200,
                sorter: (a, b) => a.abilityUri.localeCompare(b.abilityUri),
            }, {
                title: '参数示例',
                dataIndex: 'paramsExample',
                width: 250,
                render: (text, record) => (
                    <pre style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
                        {text}
                    </pre>
                ),
            }, {
                title: '返回示例',
                dataIndex: 'resultExample',
                width: 250,
                render: (text, record) => (
                    <pre style={{ wordWrap: 'break-word', wordBreak: 'break-word' }}>
                        {text}
                    </pre>
                ),
            }],
        }
        setModules(defaultModules)
    }, [])

    const onDel = async (item) => {
        modal.confirm({
            title: lag.confirmDel,
            okText: lag.ok,
            okType: 'danger',
            cancelText: lag.cancel,
            onOk: async function onOk() {
                setLoading(true)
                await post("ability/dev/del", { abilityId: item.abilityId });
                setLoading(false)
                setRefreshTime(new Date().getTime())
            },
        });
    }

    return (
        <Spin spinning={loading}>
            <AsyncTable modules={modules} refreshTime={refreshTime}
                scroll={{ y: 'calc(100vh - 250px)' }}
            />
        </Spin>
    );
}