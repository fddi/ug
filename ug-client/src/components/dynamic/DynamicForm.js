import React, { useEffect, useState } from 'react';
import { Spin, Form, Button, Modal, Card, message, Space, Row } from 'antd';
import { CheckOutlined, RedoOutlined, FormOutlined } from '@ant-design/icons';
import { post } from "@/config/client";
import { lag } from "@/config/lag";
import { jsonToFormData } from "@/util/FetchTo";
import StringUtils from '@/util/StringUtils';
import DynamicItem from './DynamicItem';
import { useRequest } from 'ahooks';

//使用ID查询表单数据，无地址返回空
async function queryData(modules, row) {
    if (StringUtils.isEmpty(row)) {
        return new Promise((resolve) => {
            resolve(null);
        });
    }
    const rowkey = modules.ID || modules.rowKey;
    const v = row[rowkey] || row['id'];
    if (!StringUtils.isEmpty(modules.findOneApi) && !StringUtils.isEmpty(v)) {
        const params = {};
        params[rowkey] = v
        return post(modules.findOneApi, params).then((result) => result.resultData);
    }
    return new Promise((resolve) => {
        resolve(null);
    });
}

/**
 * 动态表单组件
 * **/
export default function DynamicForm(props) {
    const [messageApi, contextHolder] = message.useMessage();
    const [form] = Form.useForm();
    const [spinning, setSpinning] = useState(false);
    const [item, setItem] = useState();
    const [logo, setLogo] = useState();
    const { data, loading } = useRequest(() => queryData(props.modules, props.row),
        {
            loadingDelay: 1000,
            refreshDeps: [props.row]
        });

    useEffect(() => {
        const logo = {}
        const modules = props.modules;
        const columns = modules && modules.columns;
        columns && columns.forEach(col => {
            //组件为头像上传时的参数
            if (col.inputType === "logo" && col.showOnly != true) {
                logo.logoIndex = col.dataIndex;
                logo.logoFileIndex = col.fileIndex
            }
        })
        setLogo(logo);
    }, [props.modules]);
    //选中项变更
    useEffect(() => {
        reset(props.modules, props.row, props.extraItem)
    }, [props.row]);
    //查询返回值变更
    useEffect(() => {
        if (data != null)
            reset(props.modules, data, props.extraItem);
    }, [data])
    //额外项变更
    useEffect(() => {
        reset(props.modules, props.row, props.extraItem);
    }, [props.extraItem])
    //重置表单数据
    useEffect(() => {
        form.resetFields();
    }, [item])

    function reset(modules, item, extraItem) {
        if (StringUtils.isEmpty(item)) {
            item = {};
            item['parentId'] = "0"
        }
        if (!StringUtils.isEmpty(modules.extra) && !StringUtils.isEmpty(extraItem)) {
            const key = StringUtils.isEmpty(modules.extra.rowKey) ? "key" : modules.extra.rowKey;
            if (modules.extra.isParent) {
                item['parentId'] = extraItem[key];
            } else {
                const searchKey = StringUtils.isEmpty(modules.extra.searchKey) ? key : modules.extra.searchKey;
                //额外项保存
                item[searchKey] = extraItem[key];
            }
        }
        item.refreshTime = new Date().getTime();
        modules.columns.forEach(col => {
            const defaultValue = StringUtils.isEmpty(col.defaultValue) ? '' : col.defaultValue;
            const initialValue = StringUtils.isEmpty(item[col.dataIndex]) ? defaultValue : item[col.dataIndex];
            if (col.inputType == "select" && col.mode == 'tags') {
                if (StringUtils.isEmpty(initialValue)) {
                    item[col.dataIndex] = [];
                } else {
                    item[col.dataIndex] = JSON.parse(initialValue)
                }
            } else {
                item[col.dataIndex] = initialValue;
            }
            if (col.inputType === "date") {
                let m = item[col.dataIndex];
                if (!StringUtils.isEmpty(m)) {
                    let mStr = m.format(col.format || "YYYY-MM-DD HH:mm:ss");
                    item[col.dataIndex] = mStr;
                }
            }
        })
        setItem(item)
    }

    function renderItem(columns, row) {
        if (StringUtils.isEmpty(columns)) {
            return null;
        }
        const itemComs = [];
        for (const item of columns) {
            if (item.showOnly == true) {
                continue;
            }
            switch (item.inputType) {
                case "text":
                    itemComs.push(DynamicItem.text(item, row));
                    break;
                case "textArea":
                    itemComs.push(DynamicItem.textArea(item, row));
                    break;
                case "hidden":
                    itemComs.push(DynamicItem.hidden(item, row));
                    break;
                case "select":
                    itemComs.push(DynamicItem.select(item, row));
                    break;
                case "treeSelect":
                    itemComs.push(DynamicItem.treeSelect(item, row));
                    break;
                case "number":
                    itemComs.push(DynamicItem.number(item, row));
                    break;
                case "logo":
                    itemComs.push(DynamicItem.uploadLogo(item, row, onLogoChange));
                    break;
                case "date":
                    itemComs.push(DynamicItem.date(item, row));
                    break;
                default:
                    break;
            }
        }
        return itemComs;
    }

    const onLogoChange = (file) => {
        const newLogo = { ...logo, logoFile: file }
        setLogo(newLogo);
    }

    const onFinish = () => {
        form.validateFields().then(values => {
            const { modules, onFinish } = props;
            modules.columns.forEach(col => {
                //date组件格式化
                if (col.inputType === "date" && !StringUtils.isEmpty(values[col.dataIndex])) {
                    values[col.dataIndex] = values[col.dataIndex].format(col.format || 'YYYY-MM-DD HH:mm:ss');
                }
            })
            setSpinning(true)
            const upload = StringUtils.isEmpty(logo) ? false : true;
            if (upload) {
                //表单包含文件上传
                const formData = jsonToFormData(values);
                //logo上传
                if (logo && logo.logoIndex) {
                    if (logo.logoFile) {
                        formData.append(logo.logoFileIndex, logo.logoFile)
                    }
                    if (item && !StringUtils.isEmpty(item[logo.logoIndex])) {
                        formData.append(logo.logoIndex, item[logo.logoIndex])
                    }
                }
                post(modules.saveApi, formData, false).then((result) => {
                    setSpinning(false);
                    if (result && 200 === result.resultCode) {
                        messageApi.success(result.resultMsg);
                        onFinish && onFinish();
                        reset(props.modules, null, props.extraItem);
                    } else {
                        const msg = result ? result.resultMsg : lag.errorNetwork;
                        Modal.error({
                            title: '操作失败',
                            content: result.resultMsg,
                        });
                    }
                })
            } else {
                post(modules.saveApi, values).then((result) => {
                    setSpinning(false);
                    if (result && 200 === result.resultCode) {
                        messageApi.success(result.resultMsg);
                        onFinish && onFinish();
                    } else {
                        const msg = result ? result.resultMsg : lag.errorNetwork;
                        Modal.error({
                            title: '操作失败',
                            content: result.resultMsg,
                        });
                    }
                })
            }
        })
    }

    const handleReset = () => {
        form.resetFields();
    }

    const extra = (
        <Space>
            <Button icon={<CheckOutlined />} onClick={onFinish} type="primary">提交</Button>
            <Button icon={<RedoOutlined />} onClick={handleReset}>重置</Button>
        </Space>);
    return (
        <div style={props.style}>
            <Spin spinning={spinning || loading}>
                {contextHolder}
                <Card
                    extra={extra}
                    title={
                        <Space>
                            <FormOutlined /><span>{props.modules.title}</span>
                        </Space>}
                >
                    <Form
                        labelAlign='right'
                        labelCol={{
                            span: 8
                        }}
                        labelWrap={true}
                        initialValues={item}
                        layout="horizontal"
                        form={form}
                        size="middle"
                    >
                        <Row>
                            {renderItem(props.modules.columns, item)}
                        </Row>
                    </Form>
                </Card>
            </Spin>
        </div>
    );
}