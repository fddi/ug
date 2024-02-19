import React, { Fragment, useState } from 'react';
import { HeartOutlined, HeartFilled } from '@ant-design/icons';
import { theme } from 'antd';
const { useToken } = theme;

export default function Starred(props) {
     const [starred, SetStarred] = useState(props.starred || false);
     const { token } = useToken();
     onChange = (star) => {
          const { onChange } = this.props;
          SetStarred(star)
          onChange && onChange(star);
     }
     const { defaultColor, starredColor } = props;
     const com = starred ? (
          <HeartFilled style={{ color: starredColor || token.colorPrimary }}
               onClick={() => { onChange(false) }} />)
          : (<HeartOutlined style={{ color: defaultColor || token.colorPrimary }}
               onClick={() => { onChange(true) }} />)
     return (
          <Fragment>
               {com}
          </Fragment>
     )
}