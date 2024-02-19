import React, { Fragment, useState } from 'react';
import { Row, Col, Button, Space, Card, Spin, Drawer, Modal, message } from 'antd';
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
        props.onFinish && props.onFinish();
    }

    function onEdit(record) {
        setRow(record)
        setVisible(true)
    }

    function onAdd() {
        setRow(null)
        setVisible(true)
    }

    function onDel(row) {
        if (StringUtils.isEmpty(row)) {
            message.warning(lag.noData);
            return;
        }
        const rowKey = modules.rowKey || modules.columns[0].dataIndex;
        let key = row[rowKey] || row.key;
        if (StringUtils.isEmpty(key) || key == "0") {
            message.warn(lag.noKey);
            return;
        }
        if (StringUtils.isEmpty(key) || key == "0") {
            message.warn(lag.noKey);
            return;
        }
        const params = {};
        params[rowKey] = key;
        Modal.confirm({
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
                Modal.error({
                    title: '操作失败',
                    content: result.resultMsg,
                });
            }
            setLoading(false)
        });
    }


    const { modules } = props;
    let spanForm = 19;
    if (StringUtils.isEmpty(modules.extra)) {
        spanForm += 5;
    }
    let Extra = (null);
    if (!StringUtils.isEmpty(modules.extra)) {
        //查询分类标签控件 支持菜单和树形菜单
        switch (modules.extra.type) {
            case "tree":
                Extra = (<AsyncTree modules={modules.extra} refreshTime={refreshTime}
                    handleSelect={handleExtraSelect} />);
                break;
            case "menu":
                Extra = (<AsyncMenu modules={modules.extra} refreshTime={refreshTime}
                    handleSelect={handleExtraSelect} />);
                break;
            default:
                break;
        }
    }

    return (
        <Spin spinning={loading}>
            <Space direction='vertical'>
                <Card bodyStyle={{ padding: 0 }} bordered={false}>
                    <Space style={{
                        padding: 10
                    }} size="small">
                        <Button style={{ marginRight: 5, }} icon={<ReloadOutlined />}
                            onClick={onFinish}>重载</Button>
                        <Button style={{ marginRight: 5, }} icon={<PlusCircleOutlined />}
                            onClick={onAdd}>新增</Button>
                        {props.actions && props.actions.map((Com, index) => {
                            return <Fragment key={`action-${index}`}>{Com}</Fragment>
                        })}
                    </Space>
                </Card>
                <Row gutter={[8, 8]} style={{
                    height: 'calc(100vh - 180px)', ...props.style
                }}>
                    <Col span={5} style={{ height: '100%', }}>
                        <Card bodyStyle={{ padding: 0 }} bordered={false}>
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
            </Space>
            <Drawer width={650} open={visible} onClose={() => setVisible(false)}>
                <DynamicForm modules={modules} row={row} onFinish={onFinish} extraItem={extraItem} />
            </Drawer>
        </Spin>
    );
}