import { Card } from 'antd';
import React from 'react';
import Icon404 from '@/asset/404.svg'
import { lag } from '@/config/lag'
import Image from 'next/image';

export default function Redirect404(props) {
     return (
          <Card bordered={false} style={{ display: props.display || 'block', paddingTop: 100, textAlign: "center" }}>
               <Image alt="404" src={Icon404} />
               <br />
               <br />
               <p style={{ fontSize: 20, fontWeight: "bold" }}>{lag.error404}</p>
          </Card>
     );

}
