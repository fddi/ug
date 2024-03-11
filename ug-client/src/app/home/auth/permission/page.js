"use client"

import React, { Fragment, useEffect, useState } from 'react';
import { getAuthInfo, post } from '@/config/client';
import { Button, Row, Col, Modal, Tabs, Space, App, } from 'antd';
import { LockOutlined } from '@ant-design/icons'
import AsyncTreeSelect from '@/components/AsyncTreeSelect';
import AsyncTree from '@/components/AsyncTree';
import AsyncTable from '@/components/AsyncTable';
import PeMenu from './components/PeMenu';
import PeUser from './components/PeUser';
import PeOrg from './components/PeOrg';
import StringUtils from '@/util/StringUtils';

function getExtraModules(areaCode) {
    return {
        type: "tree",
        queryApi: "org/children",
        params: {
            areaCode,
        },
        searchKey: "orgId",
    }
}

function getModules(onChange) {
    return {
        selectType: 'checkbox',
        rowKey: "roleId",
        queryApi: "role/page-list",
        params: { orgId: '' },
        columns: [{
            title: '岗位ID',
            dataIndex: 'roleId',
            colsType: "hidden",
        }, {
            title: '机构ID',
            dataIndex: 'orgId',
            colsType: "hidden",
        }, {
            title: "操作",
            render: (text, record) => {
                return (<Button type='dashed' onClick={() => onChange(record)}>权限配置</Button>)
            },
            width: 150
        }, {
            title: '岗位名称',
            dataIndex: 'roleName',
        }, {
            title: '直属部门',
            dataIndex: 'orgName',
        }, {
            title: '岗位说明',
            dataIndex: 'roleNote',
        },],
    }
}

export default function Permission(props) {
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const [volTime, setVolTime] = useState(Date.now())
    const [item, setItem] = useState()
    const [roleIds, setRoleIds] = useState()
    const [menuIds, setMenuIds] = useState()
    const [pVisible, setPVisible] = useState()
    const [mVisible, setMVisible] = useState()
    const [extraModules, setExtraModules] = useState()
    const [areaCode, setAreaCode] = useState()
    const { message } = App.useApp();
    const [modules, setModules] = useState()

    function onChange(record) {
        setMVisible(true)
        setItem(record)
    }

    useEffect(() => {
        const areaCode = getAuthInfo().areaCode;
        setAreaCode(areaCode)
        setExtraModules(getExtraModules(areaCode))
        setModules(getModules(onChange))
    }, [])

    const onExtraSelect = (item) => {
        if (item) {
            modules.params = { orgId: item.id }
            setModules({ ...modules })
        }
    }

    const handleRoleSelect = (keys, rows) => {
        setRoleIds(keys)
    }

    const onStChange = (v) => {
        extraModules.params.areaCode = v;
        setExtraModules({ ...extraModules })
        setRefreshTime(Date.now())
        setItem(null)
    }

    const onCancel = () => {
        setPVisible(false)
        setMVisible(false)
    }

    const onSubmit = () => {
        if (StringUtils.isEmpty(menuIds)) {
            message.warning("请勾选要配置的权限！");
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
            message.warning("请勾选要配置的岗位！");
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
            <Space>
                <AsyncTreeSelect
                    onChange={onStChange} style={{ width: 150 }}
                    catalog="AREA_CODE" dictCode={areaCode}
                    placeholder="选择区域" value={areaCode} />
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
                open={pVisible}
                footer={[
                    <Button key="back" onClick={onCancel}>
                        取消
                    </Button>,
                    <Button key="submit" type="primary" onClick={onSubmit}>
                        提交
                    </Button>,
                ]}
                onCancel={onCancel}
                width={700}
            >
                <PeMenu roleIds={roleIds} vols={true}
                    volTime={volTime}
                    onCheck={onMenuCheck} />
            </Modal>
            <Modal
                title={(<span><span>
                    {`[${roleName}]`}</span>权限设置</span>)}
                open={mVisible}
                footer={null}
                onCancel={onCancel}
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