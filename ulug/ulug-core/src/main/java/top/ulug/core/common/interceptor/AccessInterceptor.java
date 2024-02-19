package top.ulug.core.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.deploy.service.AccessLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liujf on 2020-09-11.
 * 逝者如斯夫 不舍昼夜
 */

@Component
public class AccessInterceptor implements HandlerInterceptor {
    @Autowired
    AccessLogService accessLogService;
    @Autowired
    RequestUtils requestUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String appId = requestUtils.getCurrentAppId();
        String pageId = request.getParameter("pageId");
        String cid = request.getParameter("cid");
        if (StringUtils.isEmpty(cid)) {
            cid = request.getParameter("fid");
        }
        if (StringUtils.isEmpty(cid)) {
            cid = request.getParameter("sid");
        }
        accessLogService.writeBase(appId, pageId, cid, request.getRequestURI());
        return true;
    }


}
