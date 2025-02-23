# 项目介绍

> 一行命令即可启动的后台管理系统，可以作为精简、高效的脚手架使用。前后端分离，微服务后端SpringBoot3由gradle构建，使用SpringJPA、cache、quartz、token；前端使用React-Nextjs和Ant-desgin。

# 快速使用
1. 检查java环境：支持java17以上版本。
2. 下载[最新版本](https://gitee.com/fddi/ug/releases/download/0.0.1/ulug0.0.1.zip)，解压文件后在当前路径，执行命令行：
```bash
java -jar ulug-core-0.0.1.jar
```
3. 访问：http://localhost:9091/template/home.html
- 登录界面

<img src="https://fddi.github.io/ug/login.png" width="80%">


- 管理平台

<img src="https://fddi.github.io/ug/home.png" width="80%">


4. 默认使用H2数据库，修改config配置文件切换生产数据库。
## 功能清单

1.  部署管理-部署维护

|菜单名称|菜单路径|功能说明
|-|-|-|
|接口管理|部署管理-部署维护|项目提供的接口能力列表。后端参数project.scanning-ability.on: true 时启动应用自动扫描接口。|
|客户端维护|部署管理-部署维护|客户端接入维护。生成密钥校验客户端请求。|
|使用手册|部署管理-部署维护|项目使用手册|
|开发文档|部署管理-部署维护|项目二次开发文档|

2.  部署管理-业务配置

|菜单名称|菜单路径|功能说明
|-|-|-|
|菜单管理|部署管理-业务配置|维护客户端菜单，绑定接口能力实现鉴权。|
|动态界面管理|部署管理-业务配置|通过JSON配置信息，可以快速创建前端CURD管理界面。|
|系统参数配置|部署管理-业务配置|系统业务参数配置|
|定时任务|部署管理-业务配置|quartz定时任务管理|

3.  系统管理-权限管理

|菜单名称|菜单路径|功能说明
|-|-|-|
|单位管理|系统管理-权限管理|机构维护，区域->机构->部门|
|人员管理|系统管理-权限管理|机构->人员维护，账号管理|
|岗位管理|系统管理-权限管理|机构->岗位维护，岗位管理。可批量复制。|
|权限管理|系统管理-权限管理|配置岗位的权限，三个维度：岗位人员、菜单权限、部门权限。|


4.  系统管理-编码管理

|菜单名称|菜单路径|功能说明
|-|-|-|
|编码目录|系统管理-编码管理|编码目录维护。|
|编码字典|系统管理-编码管理|编码字典维护。|

5.  系统管理-消息管理

|菜单名称|菜单路径|功能说明
|-|-|-|
|编码目录|系统管理-公告栏|登录页公告栏维护|
|编码字典|系统管理-消息推送|SSE消息推送。|
|个人中心|首页|个人账户联系方式维护|
|消息中心|首页|个人账户消息|

# 二次开发

1. 后端

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
2. 前端目录

Nextjs框架搭建。使用了ant-design前端组件。

```bash
#包路径说明
    app              #页面代码
    asset            #静态资源
    component        #通用组件
    config           #客户端配置
    util             #工具箱
```
# 项目文档

[有瓜开源](https://fddi.github.io/ug/)