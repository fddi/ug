package top.ulug.core.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.servlet.ResponseUtils;
import top.ulug.core.api.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liujf on 2019/3/31.
 * 逝者如斯夫 不舍昼夜
 */

@Component
public class DevOpsInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authService.checkDevOps()) {
            return true;
        }
        ResponseUtils.writeFailMsg(response, ResultMsgEnum.RESULT_ERROR_NO_PERMISSION);
        return false;
    }

}
