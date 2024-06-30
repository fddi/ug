import React, { useEffect, useState } from 'react';
import { checkDep, post } from '@/config/client';
import { Button, Table, Row, Col, Tree, App } from 'antd';
import { DeleteOutlined, } from '@ant-design/icons'
import OrgSearchBar from './OrgSearchBar';
import ArrayUtils from '@/util/ArrayUtils';
import { useRequest } from 'ahooks';

let dataList = []
async function queryData(roleId) {
    if (roleId) {
        return post("role/org-list",
            {
                roleId,
            }).then((result) => {
                if (200 === result.resultCode) {
                    const root = result.resultData.org;
                    const unitList = result.resultData.unitList;
                    const targetKeys = [];
                    dataList = [];
                    ArrayUtils.treeToArray(root, dataList);
                    dataList.forEach(item => {
                        if (item.checked) {
                            targetKeys.push(item.key);
                        }
                    });
                    return { treeData: root.children, unitList, checkedKeys: targetKeys, }
                } else {
                    return { treeData: [], unitList: [], checkedKeys: [] }
                }
            });
    } else {
        return Promise.resolve({ treeData: [], unitList: [], checkedKeys: [] })
    }
}
export default function PeOrg(props) {
    const [value, setValue] = useState('')
    const [checkedKeys, setCheckedKeys] = useState([])
    const { data, run } = useRequest(queryData, {
        manual: true,
        loadingDelay: 1000,
        onSuccess: (data, params) => {
            setCheckedKeys(data.checkedKeys)
        }
    })
    const { message } = App.useApp();

    const columns = [
        {
            title: '管辖单位',
            dataIndex: 'title',
            width: 130,
            render: (text, record) => `${record.title}[${record.value}]`,
        }, {
            title: '操作',
            render: (text, record) =>
                (<Button icon={<DeleteOutlined />} type="link" onClick={() => onDel(record)}>删除</Button>),
        }
    ]

    useEffect(() => {
        return () => {
            dataList = []
        }
    }, [])

    useEffect(() => {
        run(props.roleId)
    }, [props.roleId])

    const onCheck = (keys, e) => {
        setCheckedKeys(keys)
        post("role/org-save", {
            roleId: props.roleId,
            keys: keys.join(','),
            saveType: '1',
        }).then((result) => {
            if (result.resultCode == 200) {
                run(props.roleId)
            }
        })
    }

    const onOrgSelect = (v) => {
        setValue(v)
        post("role/org-save", {
            roleId: props.roleId,
            saveType: '2',
            keys: v,
        }).then((result) => {
            setValue('')
            if (result.resultCode == 200) {
                run(props.roleId)
            } else {
                message.info("不必配置本单位为管辖单位.")
            }
        })
    }

    const onDel = (item) => {
        post("role/org-save", {
            roleId: props.roleId,
            saveType: '3',
            keys: item.id,
        }).then((result) => {
            if (result.resultCode == 200) {
                run(props.roleId)
            }
        })
    }

    const display = checkDep() ? 'inline-block' : 'none';
    return (
        <Row style={{ height: 450, }}>
            <Col span={12} style={{ height: '100%', padding: 5, borderRight: '1px solid #cccccc' }}>
                <OrgSearchBar onSelect={onOrgSelect} value={value}
                    style={{ width: '100%', height: 40, padding: 6, display }} />
                <Table
                    scroll={{ y: 320 }}
                    size="small"
                    pagination={false}
                    columns={columns} dataSource={data && data.unitList} />
            </Col>
            <Col span={12} style={{ height: 450, padding: 10, }}>
                <Tree
                    style={{ height: "90%", overflow: "auto", }}
                    onCheck={onCheck}
                    showLine={true}
                    checkable={true}
                    selectable={false}
                    checkedKeys={checkedKeys}
                    treeData={data && data.treeData}
                    height={450}
                />
            </Col>
        </Row>
    );
}