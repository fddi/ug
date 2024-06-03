import React from 'react';
import { Carousel, Typography } from 'antd'
import { useRequest } from 'ahooks';
import { post } from '@/config/client';

async function queryData() {
    return post('notice/public-list', { pageNo: 0, pageSize: 3 }).then(result => result.resultData.content)
}

export default function LoginNotice(props) {
    const { data } = useRequest(queryData);

    function renderNotice(data) {
        return data && data.map(item => {
            return (
                <div  key={item.noticeId}>
                    <Typography style={{ padding: 10 }}>
                        <Typography.Title level={3} style={{ color: '#ccc' }}>{item.title}</Typography.Title>
                        <Typography.Paragraph style={{ color: '#ccc' }}>
                            {item.content}
                        </Typography.Paragraph>
                        <Typography.Paragraph style={{ color: '#ccc', textAlign: 'right' }}>
                            {item.gmtModified}
                        </Typography.Paragraph>
                    </Typography>
                </div>)
        })
    }
    return (
        <Carousel autoplay={true}>
            {renderNotice(data)}
        </Carousel>
    )
}