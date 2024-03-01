import React, { useState, useEffect } from 'react';
import { Menu } from 'antd';
import { faIcon } from '../../../components/IconText';
import { TYPE_TOKEN_MENU } from '@/config/client';

export default function MenuTree(props) {
     const [menus, setMenus] = useState();
     const [selectedKeys, setSelectedKeys] = useState([]);
     useEffect(() => {
          if (props.activeMenu) {
               setSelectedKeys([props.activeMenu.key + ""]);
          }
     }, [props.activeMenu]);

     useEffect(() => {
          const newMenus = []
          props.menus && props.menus.forEach(item => {
               if (item.type != TYPE_TOKEN_MENU) {
                    newMenus.push(item)
               }
          })
          rebuildMenu(newMenus)
          setMenus(newMenus)
     }, [props.menus])

     function rebuildMenu(menus) {
          menus && menus.forEach(item => {
               if (item.icon && typeof item.icon === 'string') {
                    const com = faIcon({ name: item.icon });
                    item.icon = com;
               }
               delete item.parentId;
               if (item.children) {
                    rebuildMenu(item.children)
               }
          })
     }

     return (
          <Menu
               style={{
                    height: 'calc(100vh - 50px)',
                    overflow: 'auto',
                    paddingBottom: 48
               }}
               theme='light'
               mode={props.mode}
               onSelect={props.onSelect}
               selectedKeys={selectedKeys}
               items={menus}
          />
     );
}