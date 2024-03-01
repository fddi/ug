"use client"

import React, { Fragment, useEffect, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { getAuthInfo, getOv, post } from '@/config/client';
import { Modal, Button, message, Spin, } from 'antd';
import { CopyOutlined, } from '@ant-design/icons'
import DictTreeSelect from '@/components/AsyncTreeSelect';
import RoleSearch from './components/RoleSearch';
import StringUtils from '@/util/StringUtils';
import { lag } from '@/config/lag';

const authInfo = getAuthInfo()
const defaultModules = {
    extra: {
        type: "tree",
        queryApi: "org/children",
        params: {
            areaCode: authInfo.areaCode,
        },
        rowKey: "id",
        searchKey: "orgId",
    },
    title: "岗位管理",
    type: "table",
    rowKey: "roleId",
    searchKey: "roleName",
    saveApi: "role/save",
    delApi: "role/del",
    queryApi: "role/page-list",
    columns: [{
        title: '岗位ID',
        dataIndex: 'roleId',
        inputType: "hidden",
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
export default function RoleMgr(props) {
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const [modules, setModules] = useState(defaultModules)
    const [item, setItem] = useState()
    const [org, setOrg] = useState()
    const [visible, setVisible] = useState(false)
    const [loading, setLoading] = useState(false)
    useEffect(() => {
        async function checkOrgEdit() {
            const orgEdit = await getOv('sys_org_edit');
            const disable = orgEdit == '1' ? false : true;
            defaultModules.columns[1].disabled = disable;
            setModules({ ...defaultModules })
        }
        checkOrgEdit()
    }, [])

    const handleSelect = (item) => {
        setItem(item)
    }

    const onStChange = (v) => {
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
                actions={[<DictTreeSelect
                    onChange={onStChange} style={{ width: 150 }}
                    catalog="AREA_CODE" dictCode={authInfo.areaCode}
                    placeholder="选择区域" value={modules.extra.params.areaCode} />,
                <Button icon={<CopyOutlined />}
                    onClick={() => setVisible(true)}
                >批量复制</Button>]}
            />
            <Modal
                title={`批量复制岗位[连同功能权限复制]`}
                visible={visible}
                footer={null}
                onCancel={() => setVisible(false)}
                bodyStyle={{ padding: 0, padding: 20, }}
            >
                <Spin spinning={loading}>
                    <RoleSearch orgId={org && org.id} onFinish={onFinish} />
                </Spin>
            </Modal>
        </Fragment>
    );
}