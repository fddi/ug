"use client"
import "./home.css";
import { useRouter } from 'next/navigation'
import Image from 'next/image';
import React, { useEffect, useState } from 'react';
import { ConfigProvider, Layout, Button, Flex, App } from 'antd';
import { MenuUnfoldOutlined, MenuFoldOutlined, } from '@ant-design/icons'
import { useRequest } from 'ahooks';
import { post, getAuthInfo, getOv, exportNextPath } from '../../config/client'
import StringUtils from '../../util/StringUtils'
import MenuTree from './components/MenuTree'
import MainHeader from './components/MainHeader'
import ArrayUtils from '../../util/ArrayUtils';
import logo from '../../asset/icon.png'
import TabFragment from "./components/TabFragment";
const { Content, Sider } = Layout
let menuTree;
let menuList = []
async function queryData(localSearch, key) {
    if (localSearch && menuTree && menuTree.all) {
        for (let i = 0; i < menuTree.all.length; i++) {
            if (key == menuTree.all[i].key) {
                menuTree.left = menuTree.all[i].children;
                break;
            }
        }
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(menuTree);
            }, 1000);
        });
    }

    return post('menu/personal').then((result) => {
        if (result && result.resultCode === 200) {
            const data = result.resultData.children;
            let menu1 = JSON.stringify(data);
            menu1 = JSON.parse(menu1);
            menu1 = menu1.map(item => {
                item.children = null;
                return item;
            })
            let menu2 = [];
            if (!StringUtils.isEmpty(data) && data.length > 0) {
                menu2 = data[0].children;
            }
            menuTree = { all: data, top: menu1, left: menu2 };
            menuList = []
            ArrayUtils.treeToArray({ key: 0, children: data }, menuList);
        }
        return menuTree;
    })
}

export default function HomeLayout() {
    const [collapsed, setCollapsed] = useState(false);
    const [activeMenu, setActiveMenu] = useState();
    const [appName, setAppName] = useState('');
    const [appLogo, setAppLogo] = useState();
    const { data, loading, run } = useRequest(queryData, { loadingDelay: 1000 });
    const [account, setAccount] = useState()
    const router = useRouter();

    useEffect(() => {
        async function init() {
            let cName = await getOv("admin_app_name");
            let cLogo = await getOv("admin_app_logo");
            setAppName(cName)
            setAppLogo(cLogo)
        }
        init();
    }, [])

    useEffect(() => {
        const authInfo = getAuthInfo();
        if (StringUtils.isEmpty(authInfo.token)) {
            router.replace(exportNextPath("/login"));
        }
        setAccount(authInfo)
    }, [data])

    function onChange(activeKey) {
        const menu = menuList.find(
            item => (item.key == activeKey));
        menu && setActiveMenu(menu)
    }

    function onTopMenuSelect(e) {
        run(true, e.key);
    }

    function onSelect(e) {
        const menu = menuList.find(item => item.key == e.key);
        menu && setActiveMenu(menu)
    }

    function logout() {
        sessionStorage.clear();
        router.replace(exportNextPath("/login"))
    }

    return (
        <App>
            <Layout style={{ height: '100vh' }}>
                <Sider
                    theme='dark'
                    style={{ background: '#212121' }}
                    trigger={null}
                    className="main-sider"
                    width={200}
                    collapsible
                    collapsed={collapsed}
                    onCollapse={(c) => setCollapsed(c)}
                    id="menu_sider"
                >
                    <a className="logo" href="/home">
                        <Image src={appLogo || logo}
                            alt="logo"
                            width={64}
                            height={64} />
                        <h1 style={{ color: '#fff' }}>
                            {appName}
                        </h1>
                    </a>
                    <MenuTree onSelect={onSelect} mode='inline'
                        menus={data && data.left} collapsed={collapsed} activeMenu={activeMenu} />
                </Sider>
                <Layout style={{ height: '100vh' }}>
                    <ConfigProvider
                        theme={{
                            components: {
                                Menu: {
                                    horizontalItemSelectedBg: '#353f58',
                                    horizontalItemSelectedColor: '#fff',
                                    itemBg: '#212121',
                                    subMenuItemBg: '#212121',
                                    itemColor: '#fff',
                                    itemHoverColor: '#f2f2f2',
                                    activeBarHeight: 0,
                                },
                            },
                        }
                        }>
                        <Flex style={{ height: 50, alignItems: 'start', padding: 0, backgroundColor: "#212121" }}>
                            <Button
                                type="text"
                                icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                                onClick={() => setCollapsed(!collapsed)}
                                style={{
                                    fontSize: '16px',
                                    width: 50,
                                    height: 50,
                                    color: "#fff"
                                }}
                            />
                            <MainHeader nickName={account && account.nickName} logout={logout} menuList={menuList}
                                menus={data && data.top} onSelect={onTopMenuSelect}
                                setActiveMenu={setActiveMenu}
                            />
                        </Flex>
                    </ConfigProvider>
                    <Content className="content">
                        <TabFragment activeMenu={activeMenu} onChange={onChange} />
                    </Content>
                </Layout>
            </Layout>
        </App>
    );
}
