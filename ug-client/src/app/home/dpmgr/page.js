"use client"

import React, { Fragment, useEffect, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { Button, Modal } from 'antd';
import { EditOutlined, } from '@ant-design/icons'
import dynamic from 'next/dynamic';
import { post } from '@/config/client';
const JSONEditor = dynamic(() => import('@/components/JSONEditor'), {
    ssr: false,
});

const modules = {
    type: "table",
    rowKey: "formId",
    searchKey: "formName",
    saveApi: "form/dev/save",
    delApi: "form/dev/del",
    queryApi: "form/dev/page-list",
    columns: [{
        title: 'ID',
        dataIndex: 'formId',
        inputType: "hidden",
        colsType: "hidden",
    },
    {
        title: "操作",
        colsType: "editAndDel"
    }, {
        title: '界面名称',
        dataIndex: 'formName',
        inputType: "text",
        width: '50%',
        required: true,
        message: "请填入界面名称"
    }, {
        title: '界面编码',
        dataIndex: 'formCode',
        inputType: "text",
        required: true,
        message: "请填入界面编码"
    }, {
        title: '配置信息',
        dataIndex: 'formMapper',
        inputType: "textArea",
        colsType: "hidden"
    }, {
        title: '状态',
        dataIndex: 'status',
        inputType: "select",
        catalog: "TF",
        required: true
    }]
};
export default function FormDataMgr(props) {
    const [disabled, setDisabled] = useState(true)
    const [item, setItem] = useState()
    const [open, setOpen] = useState(false)
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        if (item == null) setDisabled(true)
    }, [item])

    function onSelect(item) {
        setItem(item)
        setDisabled(false)
    }

    function onOk() {
        if (item == null) return;
        setLoading(true)
        post(modules.saveApi, item).then((result) => {
            setLoading(false)
            setOpen(false)
        })
    }

    function onChange(v) {
        if (item == null) return;
        item.formMapper = JSON.stringify(v);
        setItem(item)
    }

    return (
        <Fragment>
            <DynamicCurd modules={modules}
                handleSelect={onSelect}
                onFinish={() => setItem(null)}
                actions={[
                    <Button icon={<EditOutlined />} disabled={disabled} type='primary'
                        onClick={() => setOpen(true)}>配置</Button>
                ]} />
            <Modal
                title={`【${item && item.formName}】配置`}
                open={open}
                footer={<Button loading={loading} type='primary' onClick={onOk}>提交</Button>}
                onCancel={() => setOpen(false)}
                width={650}
            >
                <JSONEditor json={item && JSON.parse(item.formMapper)} onChange={onChange} />
            </Modal>
        </Fragment>
    );
}