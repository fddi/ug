"use client"

import React, { Fragment, useState } from 'react'
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { Button, Modal, } from 'antd';
import { DownloadOutlined, UploadOutlined, } from '@ant-design/icons'
import ImpExcelModal from '@/components/ImpExcelModal';
import { APIURL, } from '@/config/client';

const modules = {
    extra: {
        type: "tree",
        queryApi: "code/catalog-tree",
        key: "value",
        searchKey: "catalog",
        showVal: true,
        rowKey: 'key'
    },
    title: '数据字典',
    pageable: false,
    treeData: true,
    rowKey: "id",
    ID: "dictId",
    saveApi: "code/dev/dict-save",
    delApi: "code/dev/dict-del",
    queryApi: "code/dict-tree",
    findOneApi: "code/dict",
    columns: [{
        title: 'ID',
        dataIndex: 'dictId',
        inputType: "hidden",
        colsType: 'hidden'
    },
    {
        title: "操作",
        colsType: "editAndDel",
        width: 150
    }, {
        title: '字典名称',
        dataIndex: 'title',
        inputType: "hidden",
    }, {
        title: '字典编码',
        dataIndex: 'key',
        inputType: "hidden",
    }, {
        title: '父级id',
        dataIndex: 'parentId',
        inputType: "hidden",
        colsType: 'hidden'
    }, {
        title: '目录',
        dataIndex: 'catalog',
        inputType: "text",
        disabled: true,
        colsType: 'hidden'
    }, {
        title: '字典编码',
        dataIndex: 'dictCode',
        inputType: "text",
        width: '25%',
        required: true,
        message: "请填入字典编码",
        colsType: 'hidden'
    }, {
        title: '字典名称',
        dataIndex: 'dictName',
        inputType: "text",
        width: '25%',
        required: true,
        message: "请填入字典名称",
        colsType: 'hidden'
    }, {
        title: '说明',
        dataIndex: 'dictNote',
        inputType: "textArea",
        colsType: 'hidden'
    }, {
        title: '排序',
        dataIndex: 'dictSort',
        inputType: "number",
        colsType: 'hidden'
    }, {
        title: '图标',
        dataIndex: 'icon',
        width: '15%',
        inputType: "select",
        catalog: "icon",
        colsType: 'hidden'
    },],
}

export default function DictMgr(props) {
    const [open, setOpen] = useState();
    const [refreshTime, setRefreshTime] = useState(Date.now());
    return (
        <Fragment>
            <DynamicCurd modules={modules} refreshTime={refreshTime}
                actions={[<Button key="a-1" icon={<DownloadOutlined />}>
                    <a href={`${APIURL}/static/字典导入模板.xls`}>下载模版</a>
                </Button>,
                <Button key="a-1" icon={<UploadOutlined />} onClick={() => setOpen(true)}>导入字典数据</Button>]}
            />
            <Modal
                title={`导入数据`}
                open={open}
                footer={null}
                onCancel={() => setOpen(false)}
            >
                <ImpExcelModal api={"code/dev/file/upload"} onFinish={() => {
                    setOpen(false)
                    setRefreshTime(Date.now())
                }} refreshTime={refreshTime} />
            </Modal>
        </Fragment>
    )
}