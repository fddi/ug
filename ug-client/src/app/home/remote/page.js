"use client"

import React, { Fragment, useContext, useEffect, useState } from 'react'
import { HomeContext } from '../components/HomeContext';
import Redirect404 from '@/components/Redirect404';

export default function RemoteIframe(props) {
     const { activeMenu } = useContext(HomeContext);
     const [title, setTitle] = useState();
     const [src, setSrc] = useState();
     const [status, setStatus] = useState(0);

     useEffect(() => {
          if (activeMenu) {
               setTitle(activeMenu.label)
               setSrc(activeMenu.value)
               setStatus(1)
          }
     }, [activeMenu])
     let height = status == 1 ? 'calc(100vh - 120px)' : 0
     let display = status == 0 ? 'block' : 'none'
     return (
          <Fragment>
               <iframe
                    src={src} title={title}
                    style={{ border: 'none', height: height, width: '100%' }}></iframe>
               <Redirect404 display={display} />
          </Fragment>
     );
}