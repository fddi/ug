import { Calendar, Card, Col, Row, Statistic } from 'antd';
import React, { useState } from 'react';

export default function WorkBench(props) {
    const [deadline] = useState(Date.now() + 1000 * 60 * 30)
    function onFinish() {
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