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

export default function RootLayout({ children }) {
  const [color, setColor] = useState("#00B96B");
  const [algorithm, setAlgorithm] = useState([]);

  useEffect(() => {
    let themeConfig = localStorage.getItem('themeConfig')
    themeConfig = themeConfig && JSON.parse(themeConfig);
    themeConfig = themeConfig || {
      color: '#00B96B',
      type: 1,
      compact: 0,
    }
    localStorage.setItem('themeConfig', JSON.stringify(themeConfig))
    changeTheme(themeConfig);
  }, [])

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
