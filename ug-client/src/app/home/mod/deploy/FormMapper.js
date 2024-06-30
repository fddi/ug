"use client"

import React, { Fragment, useContext, useEffect, useState } from 'react';
import { useRequest, useUpdateEffect } from 'ahooks';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { getUrlParams, post } from '@/config/client'
import { HomeContext } from '../../HomeContext';
async function queryData(formCode) {
    if (formCode === null) {
        return Promise.resolve(null)
    }
    return post('form/mapper', { formCode }).then(result => {
        if (200 == result.resultCode) {
            const data = result.resultData;
            return JSON.parse(data.formMapper);
        } else {
            return Promise.resolve(null)
        }
    })
}

export default function FormMapper(props) {
    const { menu } = useContext(HomeContext);
    const [formCode, setFormCode] = useState()
    const { data, run } = useRequest(queryData, {
        loadingDelay: 1000,
        manual: true
    })

    useEffect(() => {
        if (menu && menu.value) {
            let params = getUrlParams(menu.value);
            setFormCode(params.get('formCode'))
        }
    }, [menu])
    useUpdateEffect(() => {
        run(formCode)
    }, [formCode])

    return (
        <Fragment>
            <DynamicCurd modules={data} />
        </Fragment>
    );
}