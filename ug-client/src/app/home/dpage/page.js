"use client"

import React, { Fragment, useEffect, useState } from 'react';
import { useRequest, useUpdateEffect } from 'ahooks';
import DynamicCurd from '@/components/dynamic/DynamicCurd';
import { getUrlParams, post } from '@/config/client'
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
    const [formCode, setFormCode] = useState()
    const { data, run } = useRequest(queryData, {
        loadingDelay: 1000,
        manual: true
    })

    useEffect(() => {
        let params = getUrlParams(window.location.href);
        setFormCode(params.get('formCode'))
    }, [])
    useUpdateEffect(() => {
        run(formCode)
    }, [formCode])

    return (
        <Fragment>
            <DynamicCurd modules={data} />
        </Fragment>
    );
}