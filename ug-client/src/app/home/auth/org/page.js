"use client"

import React, { Fragment, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { APIURL, getAuthInfo } from '@/config/client';
import { Button, Modal, } from 'antd';
import { DownloadOutlined, UploadOutlined } from '@ant-design/icons'
import ImpExcelModal from '@/components/ImpExcelModal';

const modules = {
    extra: {
        type: "tree",
        queryApi: "code/dict-tree",
        rowKey: "value",
        params: {
            catalog: "AREA_CODE",
            dictCode: getAuthInfo().areaCode,
        },
        searchKey: "areaCode",
    },
    title: "机构管理",
    treeData: true,
    pageable: false,
    rowKey: "orgId",
    searchKey: "orgName",
    saveApi: "org/save",
    delApi: "org/del",
    findOneApi: "org/one",
    queryApi: "org/children",
    columns: [{
        title: '机构ID',
        dataIndex: 'orgId',
        inputType: "hidden",
    }, {
        title: '父级id',
        dataIndex: 'parentId',
        inputType: "hidden",
    }, {
        title: '机构名',
        dataIndex: 'orgName',
        required: true,
        inputType: "text",
        message: "请填入名称",
    }, {
        title: '机构类型',
        dataIndex: 'orgType',
        inputType: "select",
        catalog: "org_type",
        required: true,
        message: "请选择类型",
    }, {
        title: '机构代码',
        dataIndex: 'orgCode',
        inputType: "text",
        required: true,
    }, {
        title: '地区代码',
        dataIndex: 'areaCode',
        inputType: "text",
        disabled: true,
        required: true,
    }, {
        title: '单位代码',
        dataIndex: 'unitCode',
        inputType: "text",
        required: true,
        width: 160,
        updateDisabled: true,
    }, {
        title: '机构排序',
        dataIndex: 'orgSort',
        inputType: "number",
        required: true,
        message: "请填入排序(数字1,2,3...)",
    }, {
        title: '状态',
        dataIndex: 'status',
        width: '10%',
        inputType: "select",
        catalog: "TF",
        required: true,
    },],
}

export default function OrgMgr(props) {
    const [open, setOpen] = useState(false)
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const onFinish = (result) => {
        setOpen(false)
        setRefreshTime(Date.now())
    }

    return (
        <Fragment>
            <DynamicCurd modules={modules}
                refreshTime={refreshTime}
                actions={[
                    <Button icon={<DownloadOutlined />}>
                        <a href={`${APIURL}/static/机构导入模板.xls`}>下载模版</a>
                    </Button>,
                    <Button icon={<UploadOutlined />} onClick={() => setOpen(true)}>
                        导入机构数据</Button>]} />
            <Modal
                title={`导入数据`}
                open={open}
                footer={null}
                onCancel={() => setOpen(false)}
            >
                <ImpExcelModal api="org/dev/file/upload" onFinish={onFinish} />
            </Modal>
        </Fragment>
    );
}