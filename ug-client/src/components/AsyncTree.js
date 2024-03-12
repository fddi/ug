import React, { Fragment, useEffect, useState } from 'react';
import { Tree, Input, App, } from 'antd';
import { post } from "@/config/client";
import StringUtils from '@/util/StringUtils';
import ArrayUtils from '@/util/ArrayUtils';
import { useRequest, useUpdateEffect } from 'ahooks';
let dataList = []
let allRoot;
function searchTree(value) {
    if (StringUtils.isEmpty(value)) {
        return allRoot;
    }
    let selectItems = [];
    for (let i = 0; i < dataList.length; i++) {
        const item = dataList[i];
        if (StringUtils.isEmpty(item.title)) {
            continue;
        }
        const lv = item.title.toUpperCase();
        const vv = StringUtils.isEmpty(item.value) ? null : item.value.toUpperCase();
        const rv = value.toUpperCase();
        if (lv.indexOf(rv) >= 0) {
            selectItems.push(item)
        } else if (vv && vv.indexOf(rv) >= 0) {
            selectItems.push(item)
        }
    }
    let root = [];
    if (selectItems.length > 0) {
        root = selectItems;
    }
    return root;
}

async function queryData(modules, extraItem, localSearch, v) {
    if (localSearch) {
        return new Promise((resolve) => {
            resolve(searchTree(v));
        });
    }
    let params = {
        ...modules.params
    };
    if (!StringUtils.isEmpty(modules.extra)) {
        if (StringUtils.isEmpty(extraItem)) {
            return []
        }
        const key = modules.extra.rowKey;
        const searchKey = StringUtils.isEmpty(modules.extra.searchKey) ? key : modules.extra.searchKey;
        params[searchKey] = extraItem[key]
    }
    return post(modules.queryApi, params).then((result) => {
        if (result && 200 === result.resultCode) {
            const root = result.resultData;
            dataList = [];
            ArrayUtils.treeToArray(root, dataList);
            if (root && root.id == '0') {
                allRoot = root.children || [];
                return root.children || [];
            } else {
                allRoot = root ? [root] : [];
                return root ? [root] : []
            }
        } else {
            return [];
        }
    });
}

/**
 * 数据展示 树形控件
 * **/
export default function AsyncTree(props) {
    const [selectedKeys, setSelectedKeys] = useState([]);
    const { modules, extraItem, refreshTime } = props;
    const { data, run, } = useRequest(queryData,
        {
            loadingDelay: 1000,
            manual: (modules != null && !StringUtils.isEmpty(modules.extra)),
            defaultParams: [modules]
        });
    const [height, setHeight] = useState(350)
    const { message } = App.useApp()
    useEffect(() => {
        const height = window.innerHeight - 230;
        setHeight(height)
    }, [])

    useEffect(() => {
        const { defaultSelectKey, handleSelect } = props;
        if (modules == null || data == null || defaultSelectKey == null) return;
        let key = defaultSelectKey;
        if (defaultSelectKey == 0) {
            key = data[0] && data[0].key
        }
        key && setSelectedKeys([key]);
        let item;
        if (defaultSelectKey == 0) {
            item = data[0]
        } else {
            item = { key }
        }
        handleSelect && handleSelect(item)
    }, [data])

    useUpdateEffect(() => {
        run(modules, extraItem)
    }, [modules, extraItem, refreshTime])
    useUpdateEffect(() => {
        setSelectedKeys([])
    }, [extraItem])

    if (StringUtils.isEmpty(modules)) {
        return null;
    }
    
    const handleSelect = (keys, e) => {
        const { handleSelect } = props;
        if (StringUtils.isEmpty(keys) || keys.length === 0) {
            handleSelect && handleSelect(null);
        } else {
            if (modules.selectType == 'checkbox') {
                handleSelect && handleSelect(keys);
            } else {
                const item = e.node;
                if (!StringUtils.isEmpty(item.key) && item.key != 0) {
                    handleSelect && handleSelect(item);
                }
            }
        }
        setSelectedKeys(keys)
    }

    const onChange = e => {
        setSelectedKeys([]);
        const { value } = e.target;
        run(modules, extraItem, true, value);
    };

    const onDrop = info => {
        const dragNode = info.dragNode;
        if (dragNode.children != null && dragNode.children.length > 0) {
            message.warning("只能移动叶子节点数据！");
            setSelectedKeys([])
            return;
        }
        const dropNode = info.node;
        const dropId = info.dropToGap ? dropNode.parentId : dropNode.id;
        if (dropId === dragNode.parentId) {
            return;
        }
        if (StringUtils.isEmpty(modules.dragDropApi)) {
            return;
        }
        post(modules.dragDropApi, {
            dragId: dragNode.id,
            dropId
        }).then((result) => {
            if (result && 200 === result.resultCode) {
                setSelectedKeys([])
                run(modules, extraItem)
            }
        })
    }
    return (
        <Fragment>
            <Input.Search
                allowClear
                onChange={onChange} />
            <Tree
                style={props.style}
                height={height}
                onSelect={handleSelect}
                showLine={true}
                selectedKeys={selectedKeys}
                draggable
                onDrop={onDrop}
                blockNode={true}
                treeData={data}
                checkable={modules.selectType == 'checkbox'}
                titleRender={(nodeData) => {
                    let title = nodeData.title;
                    if (modules.showVal) {
                        title += "[" + nodeData.value + "]";
                    }
                    if (nodeData.status == "0") {
                        title = (<s>{title}</s>);
                    }
                    return (<span>{title}</span>)
                }}
            />
        </Fragment>
    );
}