# 开发文档

## 后端开发

### 路径说明

项目目录：ulug

Springboot3.2.2基于gradle构建。使用了JPA、cache、quartz组件。

> 尽量不使用任何XML文件。

```bash
#包路径说明
top.ulug.core.auth
    domain       #Entity实例
    dto          #传输对象
    repository   #JPA持久化接口
    service      #服务
    web          #接口定义
```
```bash
resources
    sql              #初始化数据
    static           #静态文件
    template         #前端编译部署路径
```
### 接口标准

- WrapperDTO 类提供统一的数据返回格式。

- CurdService 提供标准的CURD服务接口。

- 使用拦截器做系统鉴权。

- TaskService 提供标准的quartz定时服务接口。

### 运行
1. 运行:top.ulug.core.Application.java

### 打包
1. 将前端编译文件拷贝至resources/template目录。
2. 运行buildDependents
3. 打包完成，jar包路径：ulug\ulug-core\build\libs。

## 前端

### 路径说明

Nextjs框架搭建。使用了ant-design前端组件。

```bash
#包路径说明
    app              #页面代码
    asset            #静态资源
    component        #通用组件
    config           #客户端配置
    util             #工具箱
```

### 运行
```bash
# 需要安装nodejs
npm install
npm run dev
```

### 打包
```bash
npm run build
```

编译文件路径：ug-client\out
