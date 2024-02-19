package top.ulug.cms.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.ulug.base.spring.CommonInterceptor;
import top.ulug.core.api.interceptor.AuthInterceptor;
import top.ulug.core.api.interceptor.ClientInterceptor;
import top.ulug.core.api.interceptor.DevOpsInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by liujf on 2018/9/19.
 * 逝者如斯夫 不舍昼夜
 */

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private CommonInterceptor commonInterceptor;
    @Autowired
    private ClientInterceptor clientInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private DevOpsInterceptor devOpsInterceptor;
    private final String[] excludePaths = {
            "/error", "/static/**", "/ug/**",
    };

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(
                StandardCharsets.UTF_8);
    }

    @Bean
    public RequestInterceptor headerInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
                if (null != attributes) {
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        while (headerNames.hasMoreElements()) {
                            String name = headerNames.nextElement();
                            String values = request.getHeader(name);
                            if (name.equals("content-length")) {
                                continue;
                            }
                            template.header(name, values);
                        }
                    }
                }
            }
        };
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        addDefaultHttpMessageConverters(converters);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor);
        registry.addInterceptor(clientInterceptor).excludePathPatterns(excludePaths)
                .excludePathPatterns("/**/file/**");
        registry.addInterceptor(authInterceptor).excludePathPatterns(excludePaths);
        registry.addInterceptor(devOpsInterceptor).excludePathPatterns(excludePaths)
                .addPathPatterns("/**/dev/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

}
