import React, { useEffect, useState } from 'react';
import { AutoComplete, } from 'antd';
import StringUtils from '@/util/StringUtils';
import { post } from '@/config/client';
let orgList
let searchedKey = ''
export default function OrgSearchBar(props) {
    const [value, setValue] = useState(props.value || "")
    const [dataSource, setDataSource] = useState([])
    useEffect(() => {
        return () => {
            orgList = null
            searchedKey = ''
        }
    }, [])

    const onSearch = searchText => {
        if (StringUtils.isEmpty(searchText) || searchText.length < 2) {
            setDataSource([])
            orgList = [];
            searchedKey = '';
            return;
        }
        if (orgList != null && orgList.length > 0) {
            const source = [];
            orgList.forEach(item => {
                if (item.key.indexOf(searchText) >= 0 || item.title.indexOf(searchText) >= 0) {
                    source.push(<AutoComplete.Option key={item.key} value={item.key}>{`${item.title}[${item.key}]`}</AutoComplete.Option>);
                }
            })
            setDataSource(source)
            return;
        }
        if (!StringUtils.isEmpty(searchedKey) && searchText.indexOf(searchedKey) >= 0) {
            return;
        }
        post("org/search",
            {
                searchKey: searchText,
                orgType: '01'
            }).then((result) => {
                searchedKey = searchText;
                if (result && 200 === result.resultCode) {
                    const source = result.resultData.map(item => {
                        return (<AutoComplete.Option key={item.key} value={item.key}>{`${item.title}[${item.key}]`}</AutoComplete.Option>);
                    })
                    orgList = result.resultData;
                    setDataSource(source)
                }
            });
    }

    const onChange = v => {
        setValue(v)
    }

    const onOrgSelect = v => {
        props.onSelect && props.onSelect(v);
    }

    return (
        <AutoComplete allowClear autoFocus
            style={{ ...props.style }}
            onSearch={onSearch}
            onSelect={onOrgSelect}
            onChange={onChange}
            placeholder="输入单位编码或名称"
            value={value}
        >
            {dataSource}
        </AutoComplete>
    );
}