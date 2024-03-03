import React, { Fragment, useState } from 'react';
import { Row, Col, Button, Space, Card, Spin, Drawer, App } from 'antd';
import { ReloadOutlined, PlusCircleOutlined } from '@ant-design/icons';
import DynamicForm from './DynamicForm';
import { post } from "../../config/client";
import { lag } from "@/config/lag";
import StringUtils from '@/util/StringUtils';
import AsyncTable from '../AsyncTable';
import AsyncTree from '../AsyncTree';
import AsyncMenu from '../AsyncMenu';
import { useUpdateEffect } from 'ahooks';

/**
 * 动态CURD界面
 * 
 * **/
export default function DynamicCurd(props) {
    const [extraItem, setExtraItem] = useState();
    const [row, setRow] = useState();
    const [refreshTime, setRefreshTime] = useState(new Date().getTime());
    const [visible, setVisible] = useState(false);
    const [loading, setLoading] = useState(false);
    const { message, modal } = App.useApp();

    useUpdateEffect(() => {
        props.refreshTime && setRefreshTime(props.refreshTime)
    }, [props.refreshTime])
    if (StringUtils.isEmpty(props.modules)) {
        return null;
    }
    const handleExtraSelect = (item) => {
        setExtraItem(item)
        props.onExtraSelect && props.onExtraSelect(item);
    }

    const onRowSelect = (item) => {
        setRow(item);
        props.handleSelect && props.handleSelect(item);
    }

    const onFinish = () => {
        setRefreshTime(new Date().getTime())
        setVisible(false)
        setRow(null)
        props.onFinish && props.onFinish();
    }

    function onEdit(record) {
        setRow(record)
        setVisible(true)
    }

    function onAdd() {
        if (modules && modules.treeData === true && row) {
            let newData = { parentId: row[modules.rowKey] }
            setRow(newData)
        } else {
            setRow(null)
        }
        setVisible(true)
    }

    function onDel(row) {
        if (StringUtils.isEmpty(row)) {
            message.warning(lag.noData);
            return;
        }
        const rowKey = modules.ID || modules.rowKey;
        let key = row[rowKey] || row.id;
        if (StringUtils.isEmpty(key) || key == "0") {
            message.warning(lag.noKey);
            return;
        }
        if (StringUtils.isEmpty(key) || key == "0") {
            message.warning(lag.noKey);
            return;
        }
        const params = {};
        params[rowKey] = key;
        modal.confirm({
            title: lag.confirmDel,
            content: `${lag.del}:${rowKey}=${key}`,
            okText: lag.ok,
            okType: 'danger',
            cancelText: lag.cancel,
            onOk() {
                doDel(params);
            },
        });
    }

    function doDel(params) {
        setLoading(true)
        post(modules.delApi, params).then((result) => {
            if (200 === result.resultCode) {
                message.success(result.resultMsg);
                onFinish();
            } else {
                modal.error({
                    title: '操作失败',
                    content: result.resultMsg,
                });
            }
            setLoading(false)
        });
    }


    const { formModules, modules } = props;
    let spanForm = 19;
    if (StringUtils.isEmpty(modules.extra)) {
        spanForm += 5;
    }
    let Extra = null;
    if (!StringUtils.isEmpty(modules.extra)) {
        //查询分类标签控件 支持菜单和树形菜单
        switch (modules.extra.type) {
            case "tree":
                Extra = (<AsyncTree modules={modules.extra}
                    handleSelect={handleExtraSelect} defaultSelectKey={0} />);
                break;
            case "menu":
                Extra = (<AsyncMenu modules={modules.extra}
                    handleSelect={handleExtraSelect} defaultSelectKey={0} />);
                break;
            default:
                break;
        }
    }

    return (
        <Spin spinning={loading}>
            <Card style={{ marginBottom: 10 }} styles={{ body: { padding: 0 } }} bordered={false}>
                <Space style={{
                    padding: 10
                }} size="small">
                    <Button style={{ marginRight: 5, }} icon={<ReloadOutlined />}
                        onClick={onFinish}>重载</Button>
                    <Button style={{ marginRight: 5, }} icon={<PlusCircleOutlined />}
                        onClick={onAdd} type='primary'>新增</Button>
                    {props.actions && props.actions.map((Com, index) => {
                        return <Fragment key={`action-${index}`}>{Com}</Fragment>
                    })}
                </Space>
            </Card>
            <Row gutter={[8, 8]} style={{
                height: 'calc(100vh - 200px)', ...props.style
            }}>
                <Col span={Extra == null ? 0 : 5} style={{ height: '100%', }}>
                    <Card styles={{ body: { padding: 0 } }} bordered={false}>
                        {Extra}
                    </Card>
                </Col>
                <Col span={spanForm} style={{ height: '100%', overflowY: "auto" }}>
                    <AsyncTable
                        modules={modules} refreshTime={refreshTime}
                        extraItem={extraItem} handleSelect={onRowSelect}
                        scroll={{ x: 600, y: 'calc(100vh - 350px)' }}
                        onEdit={onEdit} onDel={onDel}
                    />
                </Col>
            </Row>
            <Drawer destroyOnClose={true} width={'calc(48vw)'} open={visible} onClose={() => setVisible(false)} closable={false}>
                <DynamicForm modules={formModules || modules} row={row} onFinish={onFinish} extraItem={extraItem} />
            </Drawer>
        </Spin>
    );
}