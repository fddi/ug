package top.ulug.core.auth.event;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by liujf on 2019/7/17.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class LogoutEventListener {

    private final Logger LOG = LoggerFactory.getLogger(LogoutEventListener.class);

    @EventListener
    public void logout(LogoutEvent event) {
        LOG.info("LogoutEventListener run...。。。。。。。。。。。。。。。。。。。。。。。。。。。");
        LOG.info(JSON.toJSONString(event));
    }
}
