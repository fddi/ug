package top.ulug.base.servlet;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.ulug.base.dto.AbilityDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.base.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by liujf on 2021/6/19.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class RequestUtils {
    @Autowired
    WebApplicationContext applicationContext;

    /**
     * 获取当前token
     *
     * @return token
     */
    public String getCurrentToken() {
        HttpServletRequest request = getCurrentRequest();
        return request == null ? null : request.getHeader("token");
    }

    /**
     * 获取当前请求appId
     *
     * @return appId
     */
    public String getCurrentAppId() {
        HttpServletRequest request = getCurrentRequest();
        return request == null ? null : request.getHeader("appId");
    }

    /**
     * 获取当前用户名
     *
     * @return userName
     */
    public String getCurrentUserName() {
        String token = getCurrentToken();
        if (token == null) {
            return null;
        }
        try {
            Claims claims = JwtUtils.pares(token);
            String userName = String.valueOf(claims.getId());
            return userName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有参数
     *
     * @param request request
     * @return params
     */
    public Map<String, String> getAllParams(HttpServletRequest request) {
        Enumeration enu = request.getParameterNames();
        Map<String, String> params = new HashMap<>();
        while (enu.hasMoreElements()) {
            String paramName = (String) enu.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }
        return params;
    }


    /**
     * 扫描接口
     *
     * @param projectId 项目id
     * @return list
     */
    public List<AbilityDTO> scanningUri(String projectId) {
        RequestMappingHandlerMapping handleMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = handleMapping.getHandlerMethods();
        List<AbilityDTO> abilities = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethod : map.entrySet()) {
            String uri = handlerMethod.getKey()
                    .getPatternsCondition().getPatterns().toString();
            uri = uri.replaceAll("\\[", "").replaceAll("]", "");
            AbilityDTO ability = new AbilityDTO();
            if (uri.equals("/error")) {
                continue;
            }
            ability.setAbilityUri(uri);
            ApiDocument apiDocument = handlerMethod.getValue()
                    .getMethodAnnotation(ApiDocument.class);
            if (apiDocument != null) {
                ability.setAbilityNote(apiDocument.note());
                ability.setParamsExample(apiDocument.paramsExample());
                ability.setResultExample(apiDocument.resultExample());
            }
            ability.setProjectId(projectId);
            abilities.add(ability);
        }
        return abilities;
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

}
