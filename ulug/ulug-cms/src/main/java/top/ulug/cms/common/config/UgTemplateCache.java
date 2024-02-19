package top.ulug.cms.common.config;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import ug.template.engine.core.CombCache;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujf on 2022/3/9.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class UgTemplateCache implements CombCache {
    private final long timeout = 2;
    @Resource(name = "redisTemplate")
    ValueOperations<String, String> combCache;

    @Override
    public void setCache(String s, String s1) {
        if (s != null) {
            combCache.set(s, s1, timeout, TimeUnit.DAYS);
        }
    }

    @Override
    public String getCache(String s) {
        if (s != null) {
            return combCache.get(s);
        }
        return null;
    }
}
