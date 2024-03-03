import React, { useState } from 'react';
import { Button } from 'antd';
import OrgSearchBar from '../../org/components/OrgSearchBar';
import AsyncTable from '@/components/AsyncTable';
const defaultModules = {
    rowKey: "roleId",
    queryApi: "role/page-list",
    selectType: 'checkbox',
    columns: [{
        title: '岗位ID',
        dataIndex: 'roleId',
        inputType: "hidden",
        colsType: "hidden",
    }, {
        title: '机构ID',
        dataIndex: 'orgId',
        colsType: "hidden",
    }, {
        title: '岗位名称',
        dataIndex: 'roleName',
        width: 120,
    }, {
        title: '直属部门',
        dataIndex: 'orgName',
        width: 150,
    }, {
        title: '岗位说明',
        dataIndex: 'roleNote',
        width: 160,
    },],
}

export default function RoleSearch(props) {
    const [selectKeys, setSelectKeys] = useState([])
    const [modules, setModules] = useState(defaultModules)

    const handleOk = () => {
        props.onFinish && props.onFinish(selectKeys);
    }

    const handleSelect = (keys, row) => {
        setSelectKeys(keys)
    }

    const onSelect = v => {
        defaultModules.params = { unitCode: v };
        setModules({ ...defaultModules })
    }

    return (
        <div>
            <OrgSearchBar onSelect={onSelect} style={{ width: '100%' }} />
            <AsyncTable modules={modules} handleSelect={handleSelect} />
            <div style={{ marginTop: 20, textAlign: "center" }}>
                <Button type="primary" size="large"
                    style={{ width: "80%" }}
                    onClick={handleOk}>确定</Button>
            </div>
        </div>
    );

}