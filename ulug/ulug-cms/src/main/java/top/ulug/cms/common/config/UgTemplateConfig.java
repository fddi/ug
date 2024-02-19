package top.ulug.cms.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ug.template.engine.core.launcher.CombExecutor;
import ug.template.engine.core.launcher.CombFactory;

/**
 * Created by liujf on 2022/3/9.
 * 逝者如斯夫 不舍昼夜
 */
@Configuration
public class UgTemplateConfig {
    @Autowired
    UgTemplateCache ugTemplateCache;

    @Bean
    public CombExecutor combExecutor() {
        try {
            CombFactory factory = new CombFactory();
            factory.fileScan("sql-template", "magic-template").cache(ugTemplateCache);
            return factory.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
