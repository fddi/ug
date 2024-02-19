import React, { useState } from 'react';
import { Select, } from 'antd';
import StringUtils from '@/util/StringUtils';
import { post } from "@/config/client";
import { useRequest, useUpdateEffect } from 'ahooks';
import * as Icons from "react-icons/bi";
import { faIcon } from './IconText';

async function queryData(catalog, dictCode, queryApi) {
    if (StringUtils.isEmpty(catalog) || catalog === "icon" || catalog === "TF") {
        return new Promise((resolve) => {
            resolve(null);
        });
    }
    return post(queryApi || 'data/dict.json', { catalog, dictCode }).then(result => result.resultData)
}

export default function AsyncSelect(props) {
    const [value, setValue] = useState(props.value || '');
    const { data } = useRequest(() => queryData(props.catalog, props.dictCode, props.queryApi),
        {
            loadingDelay: 1000,
            refreshDeps: [props.catalog, props.dictCode]
        });
    useUpdateEffect(() => {
        setValue(props.value)
    }, [props.value])

    function handleChange(v, opts) {
        setValue(v);
        props.onChange && props.onChange(v, opts);
    }

    function renderOptions(data) {
        const catalog = props.catalog;
        if (StringUtils.isEmpty(catalog)) {
            return (null);
        }
        const itemComs = [];
        switch (catalog) {
            case "icon":
                itemComs.push((<Select.Option value='' key={"icon-nvl"}>
                    无</Select.Option>));
                Object.keys(Icons).forEach(item => {
                    itemComs.push((<Select.Option value={item} key={"icon-" + item}>
                        {<span style={{ fontSize: 16 }}>{faIcon({ name: item })}{item}</span>}</Select.Option>));
                });
                break;
            case "TF":
                itemComs.push((<Select.Option value="1" key={"tf-1"}>是</Select.Option>));
                itemComs.push((<Select.Option value="0" key={"tf-0"}>否</Select.Option>));
                break;
            default:
                data && data.forEach((item) => {
                    itemComs.push((<Select.Option value={item.key}
                        key={"ds-" + item.id}>{item.title}</Select.Option>));
                })
                break;
        }
        return itemComs;
    }
    const defaultStyle = {
    }
    const autoFocus = props.autoFocus ? true : false;
    return (
        <Select style={{ ...defaultStyle, ...props.style }}
            disabled={props.disabled}
            onChange={handleChange}
            mode={props.mode}
            autoFocus={autoFocus}
            value={value}
            placeholder={props.placeholder}
            showSearch
            allowClear
        >
            {renderOptions(data)}
        </Select>
    )
}