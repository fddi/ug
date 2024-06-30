"use client"

import React, { Fragment, useEffect, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { getAuthInfo, getOv, post } from '@/config/client';
import { Modal, Button, Spin, App, } from 'antd';
import { CopyOutlined, } from '@ant-design/icons'
import DictTreeSelect from '@/components/AsyncTreeSelect';
import RoleSearch from '@/app/home/components/RoleSearch';
import StringUtils from '@/util/StringUtils';
import { lag } from '@/config/lag';

function getModules(areaCode) {
    return {
        extra: {
            type: "tree",
            queryApi: "org/children",
            params: {
                areaCode
            },
            rowKey: "id",
            searchKey: "orgId",
        },
        title: "岗位管理",
        rowKey: "roleId",
        searchKey: "roleName",
        saveApi: "role/save",
        delApi: "role/del",
        queryApi: "role/page-list",
        columns: [{
            title: '岗位ID',
            dataIndex: 'roleId',
            inputType: "hidden",
            colsType: "hidden",
        }, {
            title: "操作",
            colsType: "editAndDel",
            width: 150
        }, {
            title: '机构ID',
            dataIndex: 'orgId',
            inputType: "text",
            disabled: true,
            required: true,
            colsType: "hidden",
        }, {
            title: '岗位名称',
            dataIndex: 'roleName',
            inputType: "text",
            required: true,
            message: "请输入名称",
            width: 120,
        }, {
            title: '直属部门',
            dataIndex: 'orgName',
            width: 150,
        }, {
            title: '岗位说明',
            dataIndex: 'roleNote',
            inputType: "textArea",
        },],
    }
}

export default function RoleMgr(props) {
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const [areaCode, setAreaCode] = useState()
    const [modules, setModules] = useState()
    const [item, setItem] = useState()
    const [org, setOrg] = useState()
    const [visible, setVisible] = useState(false)
    const [loading, setLoading] = useState(false)
    const { message } = App.useApp();

    useEffect(() => {
        async function init() {
            const orgEdit = await getOv('role_org_editable');
            const disable = orgEdit == '1' ? false : true;
            const areaCode = getAuthInfo().areaCode;
            setAreaCode(areaCode)
            const defaultModules = getModules(areaCode)
            defaultModules.columns[2].disabled = disable;
            setModules({ ...defaultModules })
        }
        init()
    }, [])

    const handleSelect = (item) => {
        setItem(item)
    }

    const onStChange = (v) => {
        const defaultModules = getModules(areaCode)
        defaultModules.extra.params.areaCode = v;
        setModules({ ...defaultModules })
        setRefreshTime(Date.now())
        setItem(null)
    }

    const onFinish = (selectKeys) => {
        if (StringUtils.isEmpty(org) || org.id == '0' || StringUtils.isEmpty(selectKeys)) {
            message.error(lag.noData);
            return;
        }
        setLoading(true)
        post("role/copy",
            {
                orgId: org.id,
                selectKeys: selectKeys.join(','),
            }).then((result) => {
                setLoading(false)
                if (200 === result.resultCode) {
                    setVisible(false)
                    setRefreshTime(Date.now())
                }
            });
    }

    return (
        <Fragment>
            <DynamicCurd modules={modules} handleSelect={handleSelect}
                refreshTime={refreshTime}
                onExtraSelect={setOrg}
                actions={[<DictTreeSelect  key="a-1"
                    onChange={onStChange} style={{ width: 150 }}
                    catalog="AREA_CODE" dictCode={areaCode}
                    placeholder="选择区域" value={areaCode} />,
                <Button icon={<CopyOutlined />}  key="a-2"
                    onClick={() => setVisible(true)}
                >批量复制</Button>]}
            />
            <Modal
                title={`批量复制岗位[连同功能权限复制]`}
                open={visible}
                footer={null}
                onCancel={() => setVisible(false)}
            >
                <Spin spinning={loading}>
                    <RoleSearch orgId={org && org.id} onFinish={onFinish} />
                </Spin>
            </Modal>
        </Fragment>
    );
}