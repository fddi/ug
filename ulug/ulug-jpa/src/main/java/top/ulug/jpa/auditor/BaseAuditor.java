package top.ulug.jpa.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import top.ulug.base.servlet.RequestUtils;

import java.util.Optional;

/**
 * Created by liujf on 2019/10/8.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class BaseAuditor implements AuditorAware<String> {
    @Autowired
    RequestUtils requestUtils;

    @Override
    public Optional<String> getCurrentAuditor() {
        String userName = requestUtils.getCurrentUserName();
        userName = userName == null ? "" : userName;
        return Optional.of(userName);
    }

}
