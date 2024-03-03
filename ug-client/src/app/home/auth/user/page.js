"use client"

import React, { Fragment, useEffect, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { APIURL, getAuthInfo, getOv, post } from '@/config/client';
import { Button, App, Radio, Modal } from 'antd';
import { DownloadOutlined, UploadOutlined, ReloadOutlined } from '@ant-design/icons'
import ImpExcelModal from '@/components/ImpExcelModal';
import StringUtils from '@/util/StringUtils';
import { lag } from '@/config/lag';
import AsyncTreeSelect from '@/components/AsyncTreeSelect';

const defaultModules = {
    extra: {
        type: "tree",
        queryApi: "org/children",
        params: {
            areaCode: getAuthInfo().areaCode,
        },
        rowKey: 'id',
        searchKey: "orgId",
    },
    title: "用户管理",
    type: "table",
    rowKey: "userId",
    searchKey: "userName",
    saveApi: "user/save",
    delApi: "user/del",
    queryApi: "user/page-list",
    params: {
        status: "1",
    },
    columns: [{
        title: '用户ID',
        dataIndex: 'userId',
        inputType: "hidden",
    }, {
        title: '机构ID',
        dataIndex: 'orgId',
        inputType: "text",
        disabled: true,
        required: true,
        colsType: "hidden",
    }, {
        title: '用户名',
        dataIndex: 'userName',
        inputType: "text",
        required: true,
        pattern: "^[A-Za-z0-9]{4,16}",
        message: "4-16位字母或数字开头的登录名",
        updateDisabled: true,
        width: 120,
        render: (text, record) => {
            if (record.status == "1") {
                return (<span style={{ color: "#096dd9" }}>{text}</span>)
            }
            return (<s>{text}</s>)
        },
    }, {
        title: '姓名',
        dataIndex: 'nickName',
        inputType: "text",
        required: true,
        width: 80,
    }, {
        title: '手机号',
        dataIndex: 'phoneNumber',
        inputType: "text",
        colsType: "hidden",
    }, {
        title: '直属部门',
        dataIndex: 'orgName',
        width: 150,
    }, {
        title: '联系地址',
        dataIndex: 'address',
        inputType: "text",
    }, {
        title: '状态',
        dataIndex: 'status',
        inputType: "select",
        catalog: "TF",
        required: true,
        colsType: "hidden",
    },],
}

export default function UserMgr(props) {
    const [refreshTime, setRefreshTime] = useState(Date.now())
    const [modules, setModules] = useState(defaultModules)
    const [item, setItem] = useState()
    const [visible, setVisible] = useState(false)
    const authInfo = getAuthInfo()
    const { modal, message } = App.useApp();
    useEffect(() => {
        async function checkOrgEdit() {
            const orgEdit = await getOv('sys_org_edit');
            const disable = orgEdit == '1' ? false : true;
            defaultModules.columns[1].disabled = disable;
            setModules({ ...defaultModules })
        }
        checkOrgEdit()
    }, [])

    const onResetPwd = () => {
        if (StringUtils.isEmpty(item) || item.userId == null) {
            message.warning("请选择需要重置的用户！");
            return;
        }
        modal.confirm({
            title: '确认重置？',
            okText: lag.ok,
            okType: 'danger',
            cancelText: lag.cancel,
            onOk() {
                message.loading(lag.loading);
                post("user/reset", { userId: item.userId }).then(result => {
                    if (200 === result.resultCode) {
                        message.destroy();
                        Modal.success({
                            title: result.resultMsg,
                            content: `密码已重置为：${result.resultData}`,
                        })
                    } else {
                        message.error(result.resultMsg)
                    }
                })
            },
        });
    }

    const handleFinish = (result) => {
        setVisible(false)
        setRefreshTime(Date.now())
    }

    const onStChange = (v) => {
        defaultModules.extra.params.areaCode = v;
        setModules({ ...defaultModules })
        setRefreshTime(Date.now())
        setItem(null)
    }

    const onSwChange = (e) => {
        defaultModules.params.status = e.target.value;
        setModules({ ...defaultModules })
        setItem(null)
    }

    return (
        <Fragment>
            <DynamicCurd modules={modules} handleSelect={setItem}
                refreshTime={refreshTime}
                onExtraSelect={() => setItem(null)}
                onFinish={() => setItem(null)}
                actions={[
                    <AsyncTreeSelect
                        style={{ width: 150 }}
                        onChange={onStChange}
                        catalog="AREA_CODE" dictCode={authInfo.areaCode}
                        placeholder="选择区域" value={modules.extra.params.areaCode} />,
                    <Button icon={<DownloadOutlined />}>
                        <a href={`${APIURL}/static/用户导入模板.xls`}>下载模版</a>
                    </Button>,
                    <Button icon={<UploadOutlined />} onClick={() => setVisible(true)}>导入用户数据</Button>,
                    <Button icon={<ReloadOutlined />} type="danger" onClick={onResetPwd}>重置密码</Button>,
                    <Radio.Group onChange={onSwChange} defaultValue={1}>
                        <Radio value={1}>可用</Radio>
                        <Radio value={0}>停用</Radio>
                    </Radio.Group>
                ]}
            />
            <Modal
                title={`导入数据`}
                visible={visible}
                footer={null}
                onCancel={() => setVisible(false)}
                bodyStyle={{ padding: 0, paddingBottom: 100, }}
            >
                <ImpExcelModal api="user/dev/file/upload" onFinish={handleFinish} dataType='list' />
            </Modal>
        </Fragment>
    );

}