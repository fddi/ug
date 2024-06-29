"use client"

import React, { Fragment, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { App, Button, Modal } from 'antd';
import { SwapOutlined, } from '@ant-design/icons'
import PeAbility from './components/PeAbility';
import StringUtils from '@/util/StringUtils';

const modules = {
    title: "菜单管理",
    extra: {
        type: "menu",
        queryApi: "client/list",
        rowKey: "key",
        searchKey: "clientName",
        title: "clientNote",
        params: { pageNo: 0, pageSize: 99 },
    },
    rowKey: "key",
    ID: "menuId",
    saveApi: "menu/dev/save",
    delApi: "menu/dev/del",
    findOneApi: "menu/dev/one",
    queryApi: "menu/dev/tree",
    dragDropApi: "menu/dev/drag-drop",
    params: { parentId: 0, },
    pageable: false,
    treeData: true,
    columns: [
        {
            title: 'ID',
            dataIndex: 'key',
            inputType: "hidden",
            colsType: "hidden",
        },
        {
            title: "操作",
            colsType: "editAndDel",
            width: 150
        }, {
            title: '菜单名称',
            dataIndex: 'title',
            width: '50%',
            inputType: "hidden",
        }, {
            title: '菜单ID',
            dataIndex: 'menuId',
            colsType: "hidden",
            inputType: "hidden",
        }, {
            title: '父级id',
            dataIndex: 'parentId',
            colsType: "hidden",
            inputType: "text",
            disabled: true
        }, {
            title: '客户端',
            dataIndex: 'clientName',
            colsType: "hidden",
            inputType: "text",
            disabled: true
        }, {
            title: '菜单名称',
            dataIndex: 'menuName',
            inputType: "text",
            required: true,
            message: "请填入名称",
            colsType: "hidden",
        }, {
            title: '菜单路径',
            dataIndex: 'menuUri',
            inputType: "text",
            colsType: "hidden",
        }, {
            title: '扩展信息',
            dataIndex: 'menuNote',
            inputType: "json",
            colsType: "hidden",
        }, {
            title: '菜单类型',
            dataIndex: 'menuType',
            inputType: "select",
            catalog: "menu_type",
            colsType: "hidden",
            defaultValue: '1'
        }, {
            title: '图标',
            dataIndex: 'icon',
            inputType: "select",
            colsType: "icon",
            catalog: "icon"
        }, {
            title: '菜单排序',
            dataIndex: 'menuSort',
            inputType: "number",
            required: true,
            message: "请填入排序(数字1,2,3...)",
            colsType: "hidden",
        }, {
            title: '菜单状态',
            dataIndex: 'status',
            inputType: "select",
            colsType: 'status',
            catalog: "TF",
            required: true,
        },],
}

export default function MenuMgr(props) {
    const [visible, setVisible] = useState();
    const [menu, setMenu] = useState();
    const { message, modal } = App.useApp();

    const onExtraSelect = (item) => {
        setMenu(null)
    }

    const onSelect = (item) => {
        if (StringUtils.isEmpty(item)) {
            setMenu(null)
        } else {
            setMenu(item)
        }
    }

    const onBindClick = () => {
        if (StringUtils.isEmpty(menu)) {
            message.warning("请选中一项菜单进行绑定！")
            return;
        }
        setVisible(true)
    }
    return (
        <Fragment>
            <DynamicCurd
                modules={modules}
                onExtraSelect={onExtraSelect}
                onFinish={() => { setMenu(null) }}
                handleSelect={onSelect}
                actions={[
                    <Button key="a-1" icon={<SwapOutlined />} onClick={onBindClick}>接口绑定</Button>
                ]} />
            <Modal
                title={(<span>{`菜单[${menu && menu.title}]`}接口绑定</span>)}
                open={visible}
                footer={null}
                onCancel={() => { setVisible(false) }}
                width={650}
            >
                <PeAbility menuId={menu && menu.id} />
            </Modal>
        </Fragment>
    );
}