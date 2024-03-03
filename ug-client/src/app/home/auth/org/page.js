"use client"

import React, { Fragment, useEffect, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { APIURL, getAuthInfo } from '@/config/client';
import { Button, Modal, } from 'antd';
import { DownloadOutlined, UploadOutlined } from '@ant-design/icons'
import ImpExcelModal from '@/components/ImpExcelModal';
import StringUtils from '@/util/StringUtils';

function getModules(unitCode_inputType = 'text', orgType_value = '01') {
    return {
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
        rowKey: "id",
        ID: "orgId",
        saveApi: "org/save",
        delApi: "org/del",
        findOneApi: "org/one",
        queryApi: "org/children",
        columns: [{
            title: '机构ID',
            dataIndex: 'orgId',
            inputType: "hidden",
            colsType: 'hidden',
        }, {
            title: "操作",
            colsType: "editAndDel",
            width: 150
        }, {
            title: '机构名称',
            dataIndex: 'title',
            width: '50%',
            inputType: "hidden",
        }, {
            title: '机构代码',
            dataIndex: 'value',
            inputType: "hidden",
        }, {
            title: '父级id',
            dataIndex: 'parentId',
            inputType: "text",
            disabled: true,
            colsType: 'hidden',
        }, {
            title: '机构名',
            dataIndex: 'orgName',
            required: true,
            inputType: "text",
            message: "请填入名称",
            colsType: 'hidden',
        }, {
            title: '机构类型',
            dataIndex: 'orgType',
            inputType: "select",
            catalog: "org_type",
            disabled: true,
            colsType: 'hidden',
            defaultValue: orgType_value
        }, {
            title: '机构代码',
            dataIndex: 'orgCode',
            inputType: "text",
            required: true,
            colsType: 'hidden',
        }, {
            title: '地区代码',
            dataIndex: 'areaCode',
            inputType: "text",
            disabled: true,
            required: true,
            colsType: 'hidden',
        }, {
            title: '单位代码',
            dataIndex: 'unitCode',
            inputType: unitCode_inputType,
            updateDisabled: true,
            required: true,
            colsType: 'hidden',
        }, {
            title: '机构排序',
            dataIndex: 'orgSort',
            inputType: "number",
            required: true,
            message: "请填入排序(数字1,2,3...)",
            colsType: 'hidden',
        }, {
            title: '状态',
            dataIndex: 'status',
            width: '10%',
            inputType: "select",
            catalog: "TF",
            required: true,
            colsType: 'status',
        },],
    }
}

export default function OrgMgr(props) {
    const [open, setOpen] = useState(false)
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const [defModules, setDefModules] = useState()
    const [modules, setModules] = useState(getModules())

    useEffect(() => {
        setDefModules(getModules())
    }, [])

    const onFinish = (result) => {
        setOpen(false)
        setModules(getModules())
    }

    function onImpFinish() {
        setRefreshTime(Date.now())
        onFinish()
    }

    function handleSelect(item) {
        if (StringUtils.isEmpty(item)) {
            setModules(getModules())
        } else {
            setModules(getModules('hidden', '02'))
        }
    }

    return (
        <Fragment>
            <DynamicCurd modules={defModules} formModules={modules} handleSelect={handleSelect}
                refreshTime={refreshTime} onFinish={onFinish}
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
                <ImpExcelModal api="org/dev/file/upload" onFinish={onImpFinish} />
            </Modal>
        </Fragment>
    );
}