package top.ulug.core.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import top.ulug.base.spring.CommonInterceptor;
import top.ulug.core.common.interceptor.AccessInterceptor;
import top.ulug.core.common.interceptor.AuthInterceptor;
import top.ulug.core.common.interceptor.ClientInterceptor;
import top.ulug.core.common.interceptor.DevOpsInterceptor;

import java.nio.charset.StandardCharsets;
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
    @Autowired
    private AccessInterceptor accessInterceptor;
    @Value("${spring.application.name}")
    private String projectId;
    private final String[] excludePaths = {
            "/error", "/static/**", "/auth/**",
            "/ability/scanning-save", "/ov/one-public", "/ov/one", "/notice/public-list"
    };

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(
                StandardCharsets.UTF_8);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        addDefaultHttpMessageConverters(converters);
    }


//    @Override
//    protected void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true).maxAge(3600);
//    }

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