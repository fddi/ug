package top.ulug.core.auth.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by liujf on 2019/7/17.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class LoginEventListener {

    private final Logger LOG = LoggerFactory.getLogger(LoginEventListener.class);

    @EventListener
    public void login(LoginEvent event) {
        LOG.info("LoginEventListener run...");
    }
}
