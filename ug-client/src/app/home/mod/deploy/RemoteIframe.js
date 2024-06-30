"use client"

import React, { Fragment, useContext, useEffect, useState } from 'react'
import { HomeContext } from '../../HomeContext';
import Redirect404 from '@/components/Redirect404';
import StringUtils from '@/util/StringUtils';
import { getUrlParams } from '@/config/client';
import { Spin } from 'antd';

export default function RemoteIframe(props) {
     const { menu } = useContext(HomeContext);
     const [title, setTitle] = useState();
     const [src, setSrc] = useState();
     const [status, setStatus] = useState(0);
     const [loading, setLoading] = useState(true);

     useEffect(() => {
          let src;
          if (menu && menu.value) {
               src = menu.value
          }
          if (!StringUtils.isEmpty(src)) {
               setSrc(src)
               setStatus(1);
          } else {
               setStatus(-1);
               setLoading(false);
          }
     }, [menu])

     let height = status == 1 ? 'calc(100vh - 95px)' : 0
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