"use client"

import React, { useEffect, useState } from 'react';
import StringUtils from '@/util/StringUtils'
import { getUrlParams, post } from '@/config/client'
import { useRequest } from 'ahooks';
import { Card, Space, } from 'antd';
import DynamicForm from '@/components/dynamic/DynamicForm';
import dynamic from 'next/dynamic';
const JSONEditor = dynamic(() => import('@/components/JSONEditor'), {
    ssr: false,
});

async function queryData(formCode) {
    return post('/data/form.json', { formCode })
}

export default function DynamicDemo(props) {
    const [modules, setModules] = useState(null);

    const { data, loading, run } = useRequest(queryData, {
        loadingDelay: 1000,
        manual: true
    })

    useEffect(() => {
        let params = getUrlParams(window.location.href);
        run(params.get('formCode'))
    }, [])
    useEffect(() => {
        if (!StringUtils.isEmpty(data)) {
            if (data.resultData) {
                setModules(data.resultData.modules)
            }
        }
    }, [data])

    function onChange(v) {
        v && setModules(v)
    }

    return (
        <Space direction='vertical'>
            <Card bodyStyle={{ padding: 0 }} bordered={false}>
                {
                    modules ? <DynamicForm modules={modules} /> : (null)
                }
            </Card>
            <Card>
                <JSONEditor json={modules} onChange={onChange} />
            </Card>
        </Space>
    );
}