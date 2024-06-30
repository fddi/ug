"use client"

import React, { useState, useEffect, lazy, Suspense } from 'react';
import { Button, Tabs, Popconfirm } from 'antd';
import { ClearOutlined } from '@ant-design/icons'
import Workbench from './WorkBench'
import StringUtils from '@/util/StringUtils';
import RoutesIndex from '../router/RouteIndex';
import Redirect404 from '../mod/Redirect404';
import Hold from '../mod/Hold';
import { lag } from '@/config/lag'
import { faIcon } from '@/components/IconText';
import { HomeContext } from '../HomeContext';

const panStyle = { height: 'calc(100vh - 95px)', overflowY: "auto", overflowX: "hidden" }
function getDefaultPage({ addTabPage }) {
    return {
        key: '0',
        closable: false,
        label: '工作台',
        children: <Workbench addTabPage={addTabPage} style={panStyle} />,
    }
}

export default function TabFragment(props) {
    const [menu, addMenu] = useState();
    const [pages, setPages] = useState([getDefaultPage({ addTabPage })]);
    const [activeKey, setActiveKey] = useState();

    useEffect(() => {
        return () => {
            clearTabs()
        }
    }, [])

    useEffect(() => {
        addMenu(props.activeMenu)
    }, [props.activeMenu]);

    useEffect(() => {
        if (menu && menu.type == '2') {
            if (menu.value.indexOf('http') == 0) {
                window.open(menu.value, '_blank');
            } else {
                path = `${document.location.protocol}//${document.location.hostname}:${document.location.port}/${menu.value}`
                window.open(path, '_blank');
            }
            return;
        }
        if (menu && menu.key === 0) {
            setActiveKey("0")
            return;
        }
        let geted = false;
        if (menu == null || StringUtils.isEmpty(menu.value)) {
            return
        }
        for (var i = 0; i < pages.length; i++) {
            if (menu.key === pages[i].key) {
                geted = true;
                break;
            }
        }
        if (geted) {
            setActiveKey(menu.key)
            return;
        }
        let page = { ...menu };
        page.children = renderChildren({ item: menu });
        if (page.icon && typeof page.icon === 'string') {
            const com = faIcon({ name: page.icon });
            page.icon = com;
        }
        let newPages = [...pages]
        newPages.push(page);
        setPages(newPages)
        setActiveKey(menu.key)
    }, [menu])

    const onTabEdit = (targetKey, action) => {
        if (action !== "remove") {
            return;
        }
        let newPages = [];
        let preIndex = 0;
        pages.forEach((item, index) => {
            if (targetKey == (item.key)) {
                preIndex = index - 1;
            }
            else {
                newPages.push(item);
            }
        });
        const preActiveKey = preIndex >= 0 ? pages[preIndex].key : "0";
        let key = activeKey === targetKey ? preActiveKey : activeKey
        setPages(newPages)
        onChange(key);
    }

    function addTabPage(menu) {
        addMenu(menu)
    }

    const clearTabs = () => {
        setPages([getDefaultPage({ addTabPage })]);
        onChange("0")
    }

    const removeTabPage = (menu) => {
        onTabEdit(menu.key, 'remove');
    }

    function renderChildren({ item }) {
        let TabPage = findRoute(item)
        return (
            <HomeContext.Provider
                value={{
                    addTabPage, removeTabPage, menu
                }} >
                <Suspense fallback={<Hold />}>
                    <TabPage item={item} style={panStyle} />
                </Suspense>
            </HomeContext.Provider>
        )
    }

    function findRoute(menu) {
        let LoadableComponent = (null)
        let component = null
        let path = menu.value;
        if (menu.value.indexOf('http') == 0) {
            path = `/home/deploy/remote?src=${menu.value}`;
        }
        if (!StringUtils.isEmpty(path)) {
            let end = path.indexOf("?") > 0 ? path.indexOf("?") : path.length;
            path = path.substring(0, end);
        }
        for (let index = 0; index < RoutesIndex.routes.length; index++) {
            const route = RoutesIndex.routes[index]
            if (route.path === path) {
                component = route.component
                break
            }
        }
        if (component == null) {
            LoadableComponent = Redirect404
        } else {
            LoadableComponent = lazy(() => import(`../mod/${component}`));
        }
        return LoadableComponent
    }

    function onChange(activeKey) {
        setActiveKey(activeKey)
        if (props.onChange) {
            props.onChange(activeKey)
        }
    }

    return (
        <Tabs
            style={{ width: '100%', overflow: 'hidden' }}
            hideAdd
            tabBarStyle={{ margin: 0}}
            type="editable-card"
            items={pages}
            onChange={onChange}
            onEdit={onTabEdit}
            activeKey={activeKey}
            tabBarExtraContent={
                <Popconfirm
                    placement="left"
                    title={lag.confirmClearTabs}
                    onConfirm={clearTabs}
                >
                    <Button type='text' icon={<ClearOutlined />} style={{ marginRight: 12, }} />
                </Popconfirm>
            }
        />
    )
}