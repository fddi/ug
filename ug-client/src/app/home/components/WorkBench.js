import { Calendar, Card, Col, message, Row, Space, Statistic } from 'antd';
import React, { useState } from 'react';

export default function WorkBench(props) {
    const [deadline] = useState(Date.now() + 1000 * 60 * 30)
    function onFinish() {
        message.error("您已使用半小时！请休息下");
    }
    return (
        <Row gutter={16}>
            <Col span={12}>
                <Card>
                    <Calendar />
                </Card>
            </Col>
            <Col span={12}>
                <Card>
                    <Statistic.Countdown
                        title="倒计时" value={deadline} onFinish={onFinish} />
                </Card>
            </Col>
        </Row>
    )
}