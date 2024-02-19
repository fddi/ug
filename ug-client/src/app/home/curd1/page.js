"use client"

import React, { Fragment, useEffect, useState } from 'react';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import StringUtils from '@/util/StringUtils'
import { post } from '@/config/client'
import { useRequest } from 'ahooks';

export default function CurdDemo1(props) {
    const [modules, setModules] = useState(null);
    const { item, } = props;
    let formCode = item && item.value;
    const { data } = useRequest(() => post('/data/curd-menu.json', { formCode }), {
        loadingDelay: 1000,
    })
    useEffect(() => {
        if (!StringUtils.isEmpty(data)) {
            if (data.resultData) {
                setModules(data.resultData.formMapper)
            }
        }
    }, [data])

    return (
        <Fragment>
            <DynamicCurd modules={modules} params={modules && modules.params}
                actions={[]}
            />
        </Fragment>
    );
}