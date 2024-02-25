import { ThemeContext } from "@/components/ThemeContext";
import { Card, Form, Radio, ColorPicker, Space, Divider, } from "antd";
import { Fragment, useContext, useState } from "react";
const FormItem = Form.Item;
const { Group } = Radio;
let themeConfig
let defType
let defCompact
export default function Help(props) {
    if (themeConfig == null) {
        themeConfig = localStorage.getItem('themeConfig')
        themeConfig = themeConfig && JSON.parse(themeConfig);
        themeConfig = themeConfig || {
            color: '#00B96B',
            type: 1,
            compact: 1,
        }
        defType = themeConfig && themeConfig.type;
        defCompact = themeConfig && themeConfig.compact;
    }

    const [color, setColor] = useState(themeConfig.color);
    const [type, setType] = useState(defType);
    const [compact, setCompact] = useState(defCompact);
    const { changeTheme } = useContext(ThemeContext);
    function onChange(v, num) {
        switch (num) {
            case 1:
                themeConfig.type = v.target.value
                changeTheme(themeConfig)
                setType(v.target.value)
                break;
            case 2:
                themeConfig.color = v
                changeTheme(themeConfig)
                setColor(v)
                break;
            case 3:
                themeConfig.compact = v.target.value
                changeTheme(themeConfig)
                setCompact(v.target.value)
                break;
            default:
                break;
        }
    }

    return (<Fragment>
        <Card title='项目信息'>

        </Card>
        <Divider />
        <Card title='主题设置'>
            <FormItem label="主题">
                <Group value={type} onChange={(v) => onChange(v, 1)}>
                    <Radio value={1}>默认</Radio>
                    <Radio value={2}>暗黑</Radio>
                </Group>
            </FormItem>
            <FormItem label="主色">
                <ColorPicker value={color} showText onChange={(v, color) => onChange(color, 2)} />
            </FormItem>
            <FormItem label="宽松度">
                <Group value={compact} onChange={(v) => onChange(v, 3)}>
                    <Radio value={1}>默认</Radio>
                    <Radio value={2}>紧凑</Radio>
                </Group>
            </FormItem>
        </Card>
    </Fragment>)
}