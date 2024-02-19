package top.ulug.core.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.servlet.ResponseUtils;
import top.ulug.core.auth.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liujf on 2019/3/31.
 * 逝者如斯夫 不舍昼夜
 */

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authService.checkToken() && authService.authAbility(request.getRequestURI())) {
            return true;
        }
        ResponseUtils.writeFailMsg(response, ResultMsgEnum.RESULT_ERROR_NO_PERMISSION);
        return false;
    }

}
