import React, { useState } from 'react';
import { Transfer, Spin, } from 'antd';
import StringUtils from '@/util/StringUtils';
import { useRequest, useUpdateEffect } from 'ahooks';
import { post } from '@/config/client';
async function queryData(menuId) {
     if (StringUtils.isEmpty(menuId)) {
          return new Promise(resolve => {
               return resolve([])
          })
     }
     return post("menu/dev/ability-list", { menuId }).then(result => {
          if (result && 200 === result.resultCode) {
               return result.resultData;
          }
     });
}

export default function PeAbility(props) {
     const [targetKeys, setTargetKeys] = useState();
     const { data, loading } = useRequest(() => queryData(props.menuId), {
          loadingDelay: 1000,
          refreshDeps: [props.menuId]
     }
     );

     useUpdateEffect(() => {
          const targetKeys = [];
          data && data.forEach(item => {
               if (item.checked) {
                    targetKeys.push(item.key);
               }
          });
          setTargetKeys(targetKeys)
     }, [data])

     function handleChange(targetKeys, direction, moveKeys) {
          setTargetKeys(targetKeys)
          post("menu/dev/ability-save",
               {
                    menuId: props.menuId,
                    keys: targetKeys.join(','),
               });
     }

     return (
          <Spin spinning={loading}>
               <Transfer
                    dataSource={data}
                    showSearch
                    listStyle={{
                         width: "46%",
                         height: 400,
                    }}
                    titles={['可绑定接口', '已绑定']}
                    searchPlaceholder='输入名称URI查询'
                    notFoundContent='请选择菜单操作'
                    targetKeys={targetKeys}
                    onChange={handleChange}
                    render={item => item.title}
               />
          </Spin>
     );
}