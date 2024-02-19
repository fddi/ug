package top.ulug.core.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.servlet.ResponseUtils;
import top.ulug.core.api.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fddiljf on 2017/5/2.
 * 逝者如斯夫 不舍昼夜
 * <p>
 * 客户端认证
 */

@Component
public class ClientInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    RequestUtils requestUtils;
    @Autowired
    @Lazy
    AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        if (authService.checkClient(requestUtils.getAllParams(request))) {
            return true;
        }
        ResponseUtils.writeFailMsg(response,ResultMsgEnum.RESULT_ERROR_CLIENT_FORBIDDEN);
        return false;
    }

}
