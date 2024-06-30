import React, { useEffect, useState } from 'react';
import { post } from '@/config/client';
import { Row, Col, Tree, App } from 'antd';
import AsyncMenu from '@/components/AsyncMenu';
import ArrayUtils from '@/util/ArrayUtils';
import { useRequest } from 'ahooks';

let dataList = []
async function queryData(clientName, roleId) {
    if (roleId) {
        return post("role/menu-list",
            {
                clientName,
                roleId,
            }).then((result) => {
                if (200 === result.resultCode) {
                    const root = result.resultData;
                    const targetKeys = [];
                    dataList = [];
                    ArrayUtils.treeToArray(root, dataList);
                    dataList.forEach(item => {
                        if (item.checked) {
                            targetKeys.push(item.key);
                        }
                    });
                    return { treeData: root.children || [], checkedKeys: targetKeys, }
                } else {
                    return { treeData: [], checkedKeys: [] }
                }
            });
    } else {
        return post("menu/vols",
            {
                clientName,
            }).then((result) => {
                if (200 === result.resultCode) {
                    const root = result.resultData;
                    return { treeData: root.children, checkedKeys: [], }

                } else {
                    return { treeData: [], checkedKeys: [] }
                }
            });
    }
}

const modules = {
    queryApi: "client/list",
}
export default function PeMenu(props) {
    const [clientName, setClientName] = useState()
    const [checkedKeys, setCheckedKeys] = useState([])
    const { data, run } = useRequest(queryData, {
        manual: true,
        loadingDelay: 1000,
        onSuccess: (data, params) => {
            setCheckedKeys(data.checkedKeys)
        }
    })

    useEffect(() => {
        return () => {
            dataList = []
        }
    }, [])

    useEffect(() => {
        dataList = []
        clientName && run(clientName, props.roleId)
    }, [props.roleId, props.volTime])

    const onSelect = (item) => {
        setClientName(item.key)
        run(item.key, props.roleId)
    }

    const onCheck = (keys, e) => {
        setCheckedKeys(keys)
        if (props.vols) {
            props.onCheck && props.onCheck(keys);
            return
        }
        post("role/menu-save", {
            roleId: props.roleId,
            clientName,
            keys: keys.join(','),
        })
    }

    return (
        <Row style={{ height: 450, }}>
            <Col span={8} style={{ height: 450, padding: 10, }}>
                <AsyncMenu modules={modules} handleSelect={onSelect} />
            </Col>
            <Col span={16} style={{ height: 450, padding: 10, }}>
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