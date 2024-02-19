package top.ulug.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by fddiljf on 2016/12/9.
 * 逝者如斯夫 不舍昼夜
 */

@SpringBootApplication(scanBasePackages = {"top.ulug"})
@EnableTransactionManagement
@EnableJpaAuditing
@EnableAsync
@EnableEurekaClient
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
