/* 
*  静态路由配置表/home 路由下TAB组件加载
*  component相对于home/components目录
*/
export default {
    routes: [
        { path: '/home/deploy/dev-menu', component: 'deploy/MenuMgr' },
        { path: '/home/deploy/dev-ability', component: 'deploy/AbilityList' },
        { path: '/home/deploy/dpage', component: 'deploy/FormMapper' },
        { path: '/home/deploy/dpmgr', component: 'deploy/FormDataMgr' },
        { path: '/home/deploy/remote', component: 'deploy/RemoteIframe' },
        { path: '/home/deploy/task', component: 'deploy/QuartzMgr' },
        { path: '/home/deploy/msg_send', component: 'deploy/MultiMessageSend' },
        { path: '/home/dict', component: 'DictMgr' },
        { path: '/home/auth/org', component: 'auth/OrgMgr' },
        { path: '/home/auth/user', component: 'auth/UserMgr' },
        { path: '/home/auth/role', component: 'auth/RoleMgr' },
        { path: '/home/auth/permission', component: 'auth/Permission' },
        { path: '/home/account_info', component: 'AccountInfo' },
    ]
}