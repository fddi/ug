package top.ulug.core.common;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import top.ulug.base.dto.AbilityDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.core.deploy.service.AbilityService;

import java.util.List;

/**
 * Created by liujf on 2019/10/11.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class AppRunner implements ApplicationRunner {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${project.scanning-ability.on}")
    private boolean onScanning;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    AbilityService abilityService;

    private final Logger LOG = LoggerFactory.getLogger(AppRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (onScanning) {
            RequestContextHolder.getRequestAttributes();
            List<AbilityDTO> list = requestUtils.scanningUri(appName);
            WrapperDTO<String> wrapperDTO = abilityService.saveAbility(list, appName, appName);
            LOG.info("saveAbility: {}", wrapperDTO.toString());
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
    }

}
