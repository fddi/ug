package top.ulug.jpa.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * Created by liujf on 2022/3/9.
 * 逝者如斯夫 不舍昼夜
 */
@Configuration
public class HbConfig {

    @Bean
    public HbNative hbNative(EntityManager entityManager) {
        return new HbNative(entityManager);
    }

}
