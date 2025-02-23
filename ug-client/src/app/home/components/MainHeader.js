import React, { useState, useEffect, Fragment, } from 'react';
import { useRouter } from 'next/navigation'
import { Badge, Menu, Button, Dropdown, Space, Input, AutoComplete, Drawer, Popover, App } from 'antd';
import { PoweroffOutlined, MessageOutlined, QuestionCircleOutlined, SearchOutlined } from '@ant-design/icons'
import StringUtils from '@/util/StringUtils';
import Help from './Help';
import { APIURL, exportNextPath, getAuthInfo, post } from '@/config/client';
let sseSource = null;
const btnStyle = {
     fontSize: '16px',
     minWidth: 55,
     height: 50,
     color: "#fff"
}
const items = [
     {
          label: '个人信息',
          key: '1',
     },
     {
          type: 'divider',
     },
     {
          label: <Space>退出登录<PoweroffOutlined /></Space>,
          key: '2',
     },
];
let mc = 0;

export default function HeaderView(props) {
     const [menus, setMenus] = useState();
     const [leafMenu, setLeafMenu] = useState();
     const [selectedKeys, setSelectedKeys] = useState([]);
     const [searchValue, setSearchValue] = useState('');
     const [helpOpen, setHelpOpen] = useState(false);
     const [msgCount, setMsgCount] = useState(0);
     const { notification } = App.useApp()
     const router = useRouter();

     useEffect(() => {
          mc = 0;
          const token = getAuthInfo().token;
          if (sseSource == null) {
               sseSource = new EventSource(`${APIURL}/sse/connect/${token}`);
               // 连接打开
               sseSource.onopen = function () {
                    console.log("连接打开");
               }

               // 连接错误
               sseSource.onerror = function (err) {
                    console.log("连接错误:", err);
               }

               // 接收到数据
               sseSource.onmessage = function (event) {
                    let message = event.data;
                    message = message && JSON.parse(message);
                    let data = message.data;
                    notification.open({
                         message: data.title,
                         description: data.message,
                         onClick: () => {
                              props.setActiveMenu({
                                   key: 'account_info_msg',
                                   value: '/home/account_info?target=msg',
                                   label: '消息中心',
                                   type: '1'
                              })
                         },
                    });
                    setMsgCount(mc + 1);
               }
          }
          post("multiMessage/unread-count", null).then((result) => {
               mc = parseInt(result.resultData || 0)
               setMsgCount(mc)
          })
     }, [])

     useEffect(() => {
          if (props.menus && props.menus.length > 0) {
               let sk = [props.menus[0].key + ""];
               setSelectedKeys(sk);
          }
          const newMenus = []
          props.menus && props.menus.forEach(item => {
               if (item.type != '5') {
                    delete item.parentId;
                    newMenus.push(item)
               }
          })
          setMenus(newMenus)
          let searchData = [];
          props.menuList && props.menuList.forEach(item => {
               if (!StringUtils.isEmpty(item.value)) {
                    searchData.push(item)
               }
          })
          setLeafMenu(searchData)
     }, [props.menus])

     function handleMenuSelect(e) {
          switch (parseInt(e.key)) {
               default:
                    setSelectedKeys(e.selectedKeys)
                    props.onSelect && props.onSelect(e)
                    break;
          }
     }

     function handleMenuClick(e) {
          switch (parseInt(e.key)) {
               case 1:
                    props.setActiveMenu({
                         key: 'account_info_context',
                         value: '/home/account_info?target=context',
                         label: '个人信息',
                         type: '1'
                    })
                    break;
               case 2:
                    props.logout && props.logout();
                    break;
               default:
                    break;
          }
     }

     function onSelect(v) {
          const menu = leafMenu.find((item) => item.value == v);
          menu && props.setActiveMenu(menu)
          setSearchValue('')
     }

     function toMessageCenter() {
          props.setActiveMenu({
               key: 'account_info_msg',
               value: '/home/account_info?target=msg',
               label: '消息中心',
               type: '1'
          })
     }

     return (<Fragment>
          <Menu
               key="menu-header-1"
               mode="horizontal"
               style={{ flex: 1, lineHeight: '50px', borderBottom: 0 }}
               selectedKeys={selectedKeys}
               onSelect={handleMenuSelect}
               items={menus}
          />
          <AutoComplete
               filterOption={(inputValue, option) =>
                    option.value.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1
                    || option.label.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1
               }
               defaultOpen={false}
               onSelect={onSelect}
               options={leafMenu}
               onChange={v => setSearchValue(v)}
               value={searchValue}
               style={{ width: 250, marginTop: 10, marginRight: 50 }}
          >
               <Input
                    prefix={<SearchOutlined />}
                    placeholder="搜索..."
               />
          </AutoComplete>
          <Popover placement="bottom" content={'帮助'}>
               <Button
                    type="text"
                    icon={<QuestionCircleOutlined />}
                    onClick={() => setHelpOpen(true)}
                    style={btnStyle}
               />
          </Popover>
          <Popover placement="bottom" content={'消息中心'}>
               <Button
                    type="text"
                    icon={
                         <Badge count={msgCount} size='small'>
                              <MessageOutlined style={{ color: '#fff' }} />
                         </Badge>}
                    onClick={toMessageCenter}
                    style={btnStyle}
               />
          </Popover>
          <Dropdown
               menu={{
                    items,
                    onClick: handleMenuClick
               }}
          >
               <Button type="text" style={btnStyle}>{props.nickName}</Button>
          </Dropdown>
          <Drawer title='帮助' open={helpOpen} onClose={() => setHelpOpen(false)}>
               <Help />
          </Drawer>
     </Fragment>
     );
} 