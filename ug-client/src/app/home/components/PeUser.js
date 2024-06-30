import React, { useState, useEffect } from 'react';
import { checkDep, post } from '@/config/client';
import { Spin, Transfer, } from 'antd';
import { useRequest } from 'ahooks';
import OrgSearchBar from './OrgSearchBar';

async function queryData(roleId, unitCode) {
    if (roleId) {
        return post("role/user-list",
            {
                unitCode,
                roleId,
            }).then((result) => {
                if (200 === result.resultCode) {
                    const list = result.resultData;
                    const targetKeys = [];
                    list && list.forEach(item => {
                        if (item.checked) {
                            targetKeys.push(item.key);
                        }
                    });
                    return { list, targetKeys: targetKeys, }
                } else {
                    return { list: [], targetKeys: [] }
                }
            });
    } else {
        return Promise.resolve({ list: [], targetKeys: [] })
    }
}

export default function PeUser(props) {
    const [targetKeys, setTargetKeys] = useState([])
    const [unitCode, setUnitCode] = useState()
    const [loading, setLoading] = useState(false)
    const { data, run } = useRequest(queryData, {
        manual: true,
        loadingDelay: 1000,
        onSuccess: (data, params) => {
            setTargetKeys(data.targetKeys)
        }
    })

    useEffect(() => {
        run(props.roleId)
    }, [props.roleId])

    const handleChange = async (targetKeys, direction, moveKeys) => {
        setTargetKeys(targetKeys)
        setLoading(true)
        await post("role/user-save", { roleId: props.roleId, keys: targetKeys.join(',') })
        setLoading(false)
    }

    const onOrgSelect = v => {
        if (v == unitCode) {
            return;
        }
        setUnitCode(v)
        run(props.roleId, v)
    }

    const display = checkDep() ? 'inline-block' : 'none';
    return (
        <Spin spinning={loading}>
            <OrgSearchBar onSelect={onOrgSelect} value={unitCode}
                style={{ width: '98%', marginTop: 10, display }} />
            <Transfer
                dataSource={data && data.list}
                showSearch
                style={{ margin: 5, }}
                listStyle={{
                    width: "46%",
                    height: 380,
                }}
                titles={['未添加', '已添加']}
                searchPlaceholder='输入用户名/姓名查询'
                targetKeys={targetKeys}
                onChange={handleChange}
                render={item => item.title}
            />
        </Spin>
    );
}