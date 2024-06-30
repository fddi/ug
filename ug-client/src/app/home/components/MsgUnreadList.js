"use client"

import React, { useEffect, useState } from 'react';
import { List, Skeleton, Button, Avatar, Space, Badge, } from 'antd'
import { useRequest } from 'ahooks';
import { post } from '@/config/client';

let localData = []
let pageSize = 10;
async function queryData(params, pageNo) {
     return post("multiMessage/page-unread-list", {
          pageSize,
          pageNo,
          ...params
     }).then((result) => {
          if (200 == result.resultCode) {
               return result.resultData
          }
     })
}

export default function MsgUnreadList(props) {
     const { callBackTotal, params, defaultData } = props
     const [pno, setPno] = useState(0)
     const [data, setData] = useState(defaultData);
     const [showLoad, setShowLoad] = useState(false);
     const { loading, run } = useRequest(queryData, {
          debounceWait: 1000,
          manual: true,
          onSuccess: (data) => {
               const rd = data && data.content;
               localData && localData.pop();
               localData = rd && localData.concat(rd);
               const total = data ? data.totalElements : 0;
               callBackTotal && callBackTotal(total);
               const s = total > ((pno + 1) * pageSize);
               setShowLoad(s)
               setData(localData)
          }
     })

     useEffect(() => {
          localData = []
          setPno(0)
          run(params, 0)
     }, [params])

     const onLoadMore = () => {
          let pageNo = pno + 1;
          localData && localData.push({ loading: true })
          setData(localData)
          setPno(pageNo)
          setShowLoad(false)
          run(params, pageNo)
     }

     function onRead(item) {
          if (item.readStatus == null || item.readStatus != "1") {
               post("multiMessage/read", { recordId: item.recordId })
          }
          debugger
          let newData = localData.map((d) => {
               if (d.recordId == item.recordId) {
                    d.readStatus = '1'
               }
               return d;
          })
          setData(newData)
     }

     function renderStatus(item) {
          if (item.recordId == null) {
               return null;
          }
          if (item.readStatus == null || item.readStatus == "0") {
               return <Badge status="processing" />
          }
          return <Badge status='default' />
     }

     const renderContent = item => {
          return (
               <List.Item
                    key={item.recordId}
                    actions={[renderStatus(item)]}
               >
                    <Skeleton loading={item.loading || false} active>
                         <List.Item.Meta
                              title={<a onClick={() => onRead(item)}>{item.title}</a>}
                              description={item.message}
                         />
                    </Skeleton>
               </List.Item>
          )
     }

     const loadMore = showLoad ? (
          <div style={{
               textAlign: 'center',
               marginTop: 12,
               height: 32,
               lineHeight: '32px',
          }}>
               <Button onClick={onLoadMore}>加载更多</Button>
          </div>
     ) : null;
     return (
          <List
               style={{ ...props.style }}
               itemLayout="horizontal"
               dataSource={data}
               renderItem={renderContent}
               loadMore={loadMore}
          />
     );

}