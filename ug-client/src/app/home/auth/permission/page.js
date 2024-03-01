"use client"

import React, { Fragment, useState } from 'react';
import { getAuthInfo, post } from '@/config/client';
import { Button, Row, Col, Modal, Tabs, message, Space, } from 'antd';
import { EditOutlined, LockOutlined } from '@ant-design/icons'
import AsyncTreeSelect from '@/components/AsyncTreeSelect';
import AsyncTree from '@/components/AsyncTree';
import AsyncTable from '@/components/AsyncTable';
import PeMenu from './components/PeMenu';
import PeUser from './components/PeUser';
import PeOrg from './components/PeOrg';
import StringUtils from '@/util/StringUtils';

const authInfo = getAuthInfo()
const defaultExtraModules = {
    type: "tree",
    queryApi: "org/children",
    params: {
        areaCode: authInfo.areaCode,
    },
    searchKey: "orgId",
}
export default function RoleMgr(props) {
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const [volTime, setVolTime] = useState(Date.now())
    const [item, setItem] = useState()
    const [roleIds, setRoleIds] = useState()
    const [menuIds, setMenuIds] = useState()
    const [pVisible, setPVisible] = useState()
    const [mVisible, setMVisible] = useState()
    const [extraModules, setExtraModules] = useState(defaultExtraModules)

    const defautModules = {
        selectType: 'checkbox',
        rowKey: "roleId",
        queryApi: "role/page-list",
        params: { orgId: '' },
        columns: [{
            title: '岗位ID',
            dataIndex: 'roleId',
            inputType: "hidden",
        }, {
            title: '机构ID',
            dataIndex: 'orgId',
            colsType: "hidden",
        }, {
            title: '岗位名称',
            dataIndex: 'roleName',
            width: 160,
            render: (text, record) =>
            (<Button icon={<EditOutlined />} type="link" onClick={() => {
                setMVisible(true)
                setItem(record)
            }}>{text}</Button>),
        }, {
            title: '直属部门',
            dataIndex: 'orgName',
            width: 160,
        }, {
            title: '岗位说明',
            dataIndex: 'roleNote',
        },],
    }

    const [modules, setModules] = useState(defautModules)


    const onExtraSelect = (item) => {
        if (item) {
            defautModules.params = { orgId: item.id }
            setModules({ ...defautModules })
        }
    }

    const handleRoleSelect = (keys, rows) => {
        setRoleIds(keys)
    }

    const onStChange = (v) => {
        defaultExtraModules.params.areaCode = v;
        setExtraModules({ ...defautModules })
        setRefreshTime(Date.now())
        setItem(null)
    }

    const onCancel = () => {
        setPVisible(false)
        setMVisible(false)
    }

    const onSubmit = () => {
        if (StringUtils.isEmpty(menuIds)) {
            message.warn("请勾选要配置的权限！");
            return;
        }
        post("role/menu-vols-save", {
            menuIds: menuIds.join(','),
            roleIds: roleIds.join(','),
        }).then((result) => {
            if (200 === result.resultCode) {
                setMenuIds([])
                setPVisible(false)
                message.success(result.resultMsg);
            } else {
                message.error(result.resultMsg);
            }
        })
    }

    const onMenuCheck = (keys) => {
        setMenuIds(keys)
    }

    const handlePm = () => {
        if (StringUtils.isEmpty(roleIds)) {
            message.warn("请勾选要配置的岗位！");
            return;
        }
        setMenuIds([])
        setPVisible(true)
        setVolTime(Date.now())
    }

    const roleName = item ? item.roleName : '';
    const roleId = item ? item.roleId : '';
    return (
        <Fragment>
            <Space size="small" style={{ width: '100%', backgroundColor: "#fff", padding: 10, marginBottom: 5, }}>
                <AsyncTreeSelect
                    onChange={onStChange} style={{ width: 150 }}
                    catalog="AREA_CODE" dictCode={authInfo.areaCode}
                    placeholder="选择区域" value={extraModules.params.areaCode} />
                <Button icon={<LockOutlined />}
                    onClick={handlePm}>批量授权</Button>
            </Space>
            <Row gutter={10} style={{ flex: '1', height: '76vh' }}>
                <Col span={5} style={{ padding: 18, backgroundColor: "#fff" }}>
                    <AsyncTree modules={extraModules} refreshTime={refreshTime}
                        handleSelect={onExtraSelect} />
                </Col>
                <Col span={19}>
                    <AsyncTable modules={modules} handleSelect={handleRoleSelect} />
                </Col>
            </Row>
            <Modal
                title={(<span>批量设置权限</span>)}
                visible={pVisible}
                footer={[
                    <Button key="back" onClick={onCancel}>
                        取消
                    </Button>,
                    <Button key="submit" type="primary" onClick={onSubmit}>
                        提交
                    </Button>,
                ]}
                onCancel={onCancel}
                bodyStyle={{ padding: 0, }}
                width={700}
            >
                <PeMenu roleIds={roleIds} vols={true}
                    volTime={volTime}
                    onCheck={onMenuCheck} />
            </Modal>
            <Modal
                title={(<span><span>
                    {`[${roleName}]`}</span>权限设置</span>)}
                visible={mVisible}
                footer={null}
                onCancel={onCancel}
                bodyStyle={{ paddingTop: 0 }}
                width={700}
            >
                <Tabs defaultActiveKey="1"
                    tabPosition='left'
                    tabBarStyle={{ marginBottom: 0, }}>
                    <Tabs.TabPane tab="菜单权限" key="1">
                        <PeMenu roleId={roleId} vols={false}
                            volTime={volTime} />
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="部门权限" key="3">
                        <PeOrg roleId={roleId} />
                    </Tabs.TabPane>
                    <Tabs.TabPane tab="人员配置" key="2">
                        <PeUser roleId={roleId} />
                    </Tabs.TabPane>
                </Tabs>
            </Modal>
        </Fragment>
    );
}