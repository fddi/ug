"use client"
import "./home.css";
import { useRouter } from 'next/navigation'
import Image from 'next/image';
import React, { useEffect, useState } from 'react';
import { ConfigProvider, Layout, Button, Flex, Breadcrumb, Space, App } from 'antd';
import { MenuUnfoldOutlined, MenuFoldOutlined, HomeOutlined } from '@ant-design/icons'
import { useRequest } from 'ahooks';
import { APPNMAE, post, getAuthInfo } from '../../config/client'
import StringUtils from '../../util/StringUtils'
import MenuTree from './components/MenuTree'
import MainHeader from './components/MainHeader'
import ArrayUtils from '../../util/ArrayUtils';
import logo from '../../asset/icon.png'
import { HomeContext } from "./components/HomeContext";
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

export default function HomeLayout({ children }) {
    const [collapsed, setCollapsed] = useState(false);
    const [activeMenu, setActiveMenu] = useState();
    const { data, loading, run } = useRequest(queryData, { loadingDelay: 1000 });
    const [account, setAccount] = useState()
    const router = useRouter();

    useEffect(() => {
        const authInfo = getAuthInfo();
        if (StringUtils.isEmpty(authInfo.token)) {
            router.replace("/login");
        }
        setAccount(authInfo)
    }, [data])

    function onTopMenuSelect(e) {
        run(true, e.key);
    }

    function onSelect(e) {
        const menu = menuList.find(item => item.key == e.key);
        menu && onMenuRoute(menu, true)
    }

    function onMenuRoute(menu, isRoute) {
        setActiveMenu(menu)
        if (isRoute === false) {
            return;
        }
        let path;
        switch (menu.type) {
            case "1":
                path = menu.value;
                if (menu.value.indexOf('http') == 0) {
                    path = `/home/deploy/remote?url=${menu.value}`;
                }
                router.push(path)
                break;
            case "2":
                if (menu.value.indexOf('http') == 0) {
                    window.open(menu.value, '_blank');
                } else {
                    path = `${document.location.protocol}//${document.location.hostname}:${document.location.port}/${menu.value}`
                    window.open(path, '_blank');
                }
                break;
            default:
                break;
        }
    }

    function logout() {
        sessionStorage.clear();
        router.replace("/login")
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
                        <Image src={logo}
                            alt="logo"
                            width={64}
                            height={64} />
                        <h1 style={{ color: '#fff' }}>
                            {APPNMAE}
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
                                onMenuRoute={onMenuRoute}
                            />
                        </Flex>
                    </ConfigProvider>
                    <Content className="content">
                        <div style={{ margin: 10, marginBottom: 20 }}>
                            <Breadcrumb items={[{
                                href: '/home',
                                title: <HomeOutlined />,
                            }, { title: activeMenu ? activeMenu.label : '工作台' },]} />
                        </div>
                        <HomeContext.Provider value={{ activeMenu }} handleMenu={setActiveMenu}>
                            {children}
                        </HomeContext.Provider>
                    </Content>
                </Layout>
            </Layout>
        </App>
    );
}
