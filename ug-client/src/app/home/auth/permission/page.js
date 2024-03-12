"use client"

import React, { Fragment, useEffect, useState } from 'react';
import { getAuthInfo, post } from '@/config/client';
import { Button, Row, Col, Modal, Tabs, Card, Space, App, Divider, } from 'antd';
import { LockOutlined } from '@ant-design/icons'
import { BiSlider } from "react-icons/bi";
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
        dragDropApi: "org/drag-drop",
        rowKey: 'id',
        searchKey: "orgId",
        params: {
            areaCode,
        },
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
                return (<Button icon={<BiSlider />} onClick={() => onChange(record)} shape='circle' size='small' />)
            },
            width: 60
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
            <Card style={{ marginBottom: 10 }} styles={{ body: { padding: 0 } }} bordered={false}>
                <Space style={{
                    padding: 10
                }} size="small">
                    <AsyncTreeSelect
                        onChange={onStChange} style={{ width: 150 }}
                        catalog="AREA_CODE" dictCode={areaCode}
                        placeholder="选择区域" value={areaCode} />
                    <Button icon={<LockOutlined />}
                        onClick={handlePm}>批量授权</Button>
                </Space>
            </Card>
            <Row gutter={[8, 8]} style={{
                height: 'calc(100vh - 200px)', ...props.style
            }}>
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
                <Divider />
                <Tabs defaultActiveKey="1"
                    tabPosition='left'
                    tabBarStyle={{ marginBottom: 0, }}
                    items={[{
                        key: '1',
                        label: "菜单权限",
                        children: <PeMenu roleId={roleId} vols={false}
                            volTime={volTime} />,
                    }, {
                        key: '2',
                        label: "部门权限",
                        children: <PeOrg roleId={roleId} />,
                    }, {
                        key: '3',
                        label: "人员配置",
                        children: <PeUser roleId={roleId} />,
                    },]}>
                </Tabs>
            </Modal>
        </Fragment>
    );
}