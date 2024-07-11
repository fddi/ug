# 使用手册
## 打包文件目录
- ulug-core-x.x.x.jar  对应版本号编译后jar包，包含前端编译文件。
- ugDB.mv.db，ugDB.trace.db 初始化H2数据库文件。
- config文件夹 配置文件。

## 启动

```bash
#使用java17+
java -jar ulug-core-0.0.1.jar
#设置最小堆内存
java -jar -Xms512M -Xmx512M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=128M ulug-core-0.0.1.jar
```
访问：http://localhost:9091/template/home.html

## 配置文件

可在jar包目录下config文件夹添加或修改配置文件。SpringBoot默认会优先读取。

application.yml

> Springboot主配置文件

修改此参数引入更多配置文件：

> spring.profiles.active=security,dev


application-security.yml

> 安全相关配置。通过拦截器、Token组建的一个简单安全框架。

application-dev.yml

> 开发环境配置文件

application-pro.yml

> 生产环境配置文件

数据库连接池使用：hikari。

数据持久化访问方式： SpringBoot JPA。

日志使用默认框架： logback。

## 切换数据库

以PG数据库为例

1. 修改配置文件
```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5588/postgres?currentSchema=ug
    username: postgres
    password: ug
    driverClassName: org.postgresql.Driver
```
2. 执行[data.sql](..\ulug\ulug-core\src\main\resources\sql\data.sql)，初始化数据.
3. 执行[tables_postgres.sql](..\ulug\ulug-core\src\main\resources\sql\quartz\tables_postgres.sql)，初始化quartz定时任务持久化对象。

## 系统鉴权
### 客户端鉴权
    所有客户端请求头需要添加appId和sign签名以做鉴权。使用传参的参数按照accsii排序加客户端密钥生成SHA256签名。

### 用户Token认证
    登录后分配Token，使用JWT算法和缓存进行Token有效认证。

### 用户权限

通过岗位（角色）授权使用权限

菜单权限：

    接口能力绑定->菜单->岗位->用户

部门权限：

    区域->部门->岗位->用户

岗位和用户可以是多对多关系。

## 系统参数配置
菜单：部署管理-业务配置-系统业务参数配置 可动态配置系统参数。

- admin_app_name：修改系统显示名称。
- admin_app_logo：修改系统图标。（src）
- role_org_editable: 岗位所属机构是否可修改。（测试）

## 动态界面配置

菜单：部署管理-业务配置-动态界面管理 通过JSON配置信息，可以快速创建前端CURD管理界面。

1. 创建动态界面。

2. 配置信息（JSON）
```js
//ant-design表格modules数据的扩展
{
    "extra": {  //左侧数据导航栏modules配置 
        "type": "tree",  //数据导航组件配置 可用 menu | tree
        "queryApi": "code/dict-tree", //数据集查询接口
        "rowKey": "value",  //索引字段名
        "params": { //数据集查询参数
            "catalog": "optional"
        },
        "dragDropApi":"", //tree拖拽接口 type为tree时可用
        "searchKey": "optionCode", //右侧绑定字段名
        "showVal": true //是否同时显示label和value
    },
    "title": "系统配置值管理",  //标题
    "pageable": false,  //是否分页数据
    "ID": "ovId", //ID字段名
    "rowKey": "ovId", //索引字段名,同表格rowKey
    "saveApi": "ov/save", //数据保存接口
    "delApi": "ov/dev/del", //数据删除接口
    "queryApi": "ov/list", //数据查询接口
    "columns": [
            //数据字段配置
        { 
            title: '文本',//标题
            dataIndex: dataKey, //数据项名称
            inputType: "text", //可用 text | textArea | hidden | select | treeSelect | number | logo | date
            disabled:false, //是否可填写
            updateDisabled:false, //是否可修改
            required:true,//是否必填
            pattern:/^[A-Za-z0-9]{1,16}/,//校验数据的正则表达式
            message:msg //校验提示信息
            readOnly:false||"readOnly",//只读
            catalog:catalog,//目录编码，查询目录字典数据 select和treeSelect 时可用 值为"TF"为是否选项 为"icon" 图标选项
            dictCode:code,//字典编码 字典子节点数据 select和treeSelect 时可用
            mode:mode,//select可用 设置多选
            checkable:false,//treeSelect可用 设置多选
            fileIndex:fileName,//logo可用 上传文件名称
            format: "YYYY-MM-DD",//date可用 格式化日期数据
            defaultValue:"", //默认值
            //数据集展示配置
            width:number, //table列宽
            colsType: 'hidden', // hidden|icon-显示对应图标|status-显示状态|edit-显示编辑按钮|editAndDel-显示编辑和删除按钮
            //其他table参数，参照ant-design table组件
        }, ...{}
    ]
}
```
3. 新增菜单，路径：/home/deploy/dpage?formCode=界面编码
4. 访问新菜单，查看效果。

