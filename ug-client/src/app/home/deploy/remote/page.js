"use client"

import React, { Fragment, useContext, useEffect, useState } from 'react'
import { HomeContext } from '../../components/HomeContext';
import Redirect404 from '@/components/Redirect404';
import StringUtils from '@/util/StringUtils';
import { getUrlParams } from '@/config/client';
import { Spin } from 'antd';

export default function RemoteIframe(props) {
     const { activeMenu } = useContext(HomeContext);
     const [title, setTitle] = useState();
     const [src, setSrc] = useState();
     const [status, setStatus] = useState(0);
     const [loading, setLoading] = useState(true);

     useEffect(() => {
          let src;
          if (activeMenu && activeMenu.value) {
               setTitle(activeMenu.label)
               src = activeMenu.value
          } else {
               let params = getUrlParams(window.location.href);
               src = params.get('url')
          }
          if (!StringUtils.isEmpty(src)) {
               setSrc(src)
               setStatus(1);
          } else {
               setStatus(-1);
               setLoading(false);
          }
     }, [activeMenu])

     let height = status == 1 ? 'calc(100vh - 150px)' : 0
     let display = status == -1 ? 'block' : 'none'
     return (
          <Fragment>
               <Spin spinning={loading}>
                    <iframe
                         onLoad={() => setLoading(false)}
                         src={src} title={title}
                         style={{ border: 'none', height: height, width: '100%' }}></iframe>
                    <Redirect404 display={display} />
               </Spin>
          </Fragment>
     );
}