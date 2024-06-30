import { Card, Col, Layout, Row, Space, Typography } from 'antd';
import React, { useState } from 'react';
const projectData = [{
    title: 'ug',
    desc: '一行命令即可启动的后台管理系统，可以作为精简、高效的脚手架使用。前后端分离，微服务后端SpringBoot3由gradle构建，使用SpringJPA、cache、quartz、token；前端使用React-Nextjs和Ant-desgin。',
    star: () => (
        <a href='https://gitee.com/fddi/ug/stargazers'><img src='https://gitee.com/fddi/ug/badge/star.svg?theme=dark' alt='star'></img></a>
    ),
    url: 'https://gitee.com/fddi/ug'
}, {
    title: 'electron-react-quick-start',
    desc: 'electron+react 的脚手架项目。提供Electron + React 桌面程序创建，测试，打包的示例，Electron环境下调用DLL的示例。界面使用ant-design。',
    star: () => (
        <a href='https://gitee.com/fddi/electron-react-quick-start/stargazers'><img src='https://gitee.com/fddi/electron-react-quick-start/badge/star.svg?theme=dark' alt='star'></img></a>
    ),
    url: 'https://gitee.com/fddi/electron-react-quick-start'
}]


export default function WorkBench(props) {
    const defaultStyle = { background: 'linear-gradient(320deg, rgba(255, 255, 255, 0.99) 0%, rgba(247, 245, 241, 0.1) 50%, rgba(240, 254, 233, 0.99) 100%)', }

    function onPressCard(url) {
        window.open(url, '__blank');
    }
    return (
        <Layout style={{ ...props.style, padding: 16 }}>
            <Row gutter={16}>
                {
                    projectData.map((item, index) => {
                        return (
                            <Col span={8} key={"a-" + index}>
                                <Card
                                    style={{ ...defaultStyle }}
                                    hoverable={true}
                                    onClick={() => onPressCard(item.url)}
                                    bordered={false}
                                >
                                    <Space direction='vertical'>
                                        <Card.Meta
                                            title={<Typography.Title level={4}>{item.title}</Typography.Title>}
                                            description={item.desc}
                                        />
                                        {item.star()}
                                    </Space>
                                </Card>
                            </Col>
                        )
                    })
                }
            </Row>
        </Layout>
    )
}