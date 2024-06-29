package top.ulug.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2024/6/29 下午5:44 星期六
 */
@Component
@ConfigurationProperties(prefix = "interfaces")
public class SecurityIfConfig {
    private String[] resourceUrls;
    private String[] resourcePaths;
    private String[] noClient;
    private String[] noAuth;
    private String[] developer;

    public SecurityIfConfig() {
    }

    public String[] getResourceUrls() {
        return resourceUrls;
    }

    public void setResourceUrls(String[] resourceUrls) {
        this.resourceUrls = resourceUrls;
    }

    public String[] getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(String[] resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    public String[] getNoClient() {
        return noClient;
    }

    public void setNoClient(String[] noClient) {
        this.noClient = noClient;
    }

    public String[] getNoAuth() {
        return noAuth;
    }

    public void setNoAuth(String[] noAuth) {
        this.noAuth = noAuth;
    }

    public String[] getDeveloper() {
        return developer;
    }

    public void setDeveloper(String[] developer) {
        this.developer = developer;
    }
}
