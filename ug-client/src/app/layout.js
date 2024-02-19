"use client"


import { ConfigProvider, theme } from "antd";
import "./globals.css";
import { AntdRegistry } from '@ant-design/nextjs-registry';
import { useState, useEffect } from "react";
import { ThemeContext } from "@/components/ThemeContext";
import locale from 'antd/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';

dayjs.locale('zh-cn');

let themeConfig
let defAlgorithm = [];

export default function RootLayout({ children }) {
  useEffect(() => {
    if (themeConfig == null) {
      themeConfig = localStorage.getItem('themeConfig')
      themeConfig = themeConfig && JSON.parse(themeConfig);
      themeConfig = themeConfig || {
        color: '#00B96B',
        type: 1,
        compact: 0,
      }
      localStorage.setItem('themeConfig', JSON.stringify(themeConfig))
      if (themeConfig.type == '2') {
        defAlgorithm = [theme.darkAlgorithm]
      } else {
        defAlgorithm = [theme.defaultAlgorithm]
      }
      if (themeConfig.compact == '2') defAlgorithm.push(theme.compactAlgorithm)
    }
  },[])

  const [color, setColor] = useState(themeConfig ? themeConfig.color : "#00B96B");
  const [algorithm, setAlgorithm] = useState(defAlgorithm);
  function changeTheme(themeInfo) {
    if (themeInfo == null) return;
    localStorage.setItem('themeConfig', JSON.stringify(themeInfo))
    let defAlgorithm = [];
    if (themeInfo.type == '2') {
      defAlgorithm = [theme.darkAlgorithm]
    } else {
      defAlgorithm = [theme.defaultAlgorithm]
    }
    if (themeInfo.compact == '2') defAlgorithm.push(theme.compactAlgorithm)
    setColor(themeInfo.color)
    setAlgorithm(defAlgorithm)
  }

  return (
    <html lang="en">
      <body>
        <AntdRegistry>
          <ConfigProvider
            locale={locale}
            theme={{
              token: {
                colorPrimary: color,
              },
              algorithm: algorithm,
            }}
          >
            <ThemeContext.Provider value={{ changeTheme }}>
              {children}
            </ThemeContext.Provider>
          </ConfigProvider>
        </AntdRegistry>
      </body>
    </html>
  );
}
