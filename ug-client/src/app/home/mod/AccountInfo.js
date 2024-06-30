import React, { useState, useEffect, useContext } from 'react';
import { Card, Row, Col, Layout, } from 'antd';
import AccountCard from '../components/AccountCard';
import ContextInfo from '../components/ContextInfo';
import MsgUnreadList from '../components/MsgUnreadList';
import { HomeContext } from '../HomeContext';
import { getUrlParams } from '@/config/client';
export default function AccountInfo() {
    const { menu } = useContext(HomeContext);
    const [target, setTarget] = useState()

    useEffect(() => {
        if (menu && menu.value) {
            let params = getUrlParams(menu.value);
            setTarget(params.get('target'))
        }
    }, [menu])

    return (
        <Layout style={{ padding: 16 }}>
            <Row gutter={8} style={{ padding: 5, }}>
                <Col span={5}>
                    <AccountCard setTarget={setTarget} />
                </Col>
                <Col span={19}>
                    <Card>
                        {target == 'context' ?
                            <ContextInfo /> :
                            <MsgUnreadList defaultData={[{ loading: true }]} />}
                    </Card>
                </Col>
            </Row></Layout>
    )
}