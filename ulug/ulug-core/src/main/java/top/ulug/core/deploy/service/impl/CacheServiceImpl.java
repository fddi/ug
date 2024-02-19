package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.PhysicalUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.deploy.service.CacheService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by liujf on 2019/9/12.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource(name = "redisTemplate")
    ValueOperations<String, Object> vOps;
    @Value("${spring.application.name}")
    String projectId;
    @Autowired
    RequestUtils requestUtils;

    @Override
    public WrapperDTO<List<String>> getCacheSize() {
        String size = (String) redisTemplate.execute(
                (RedisCallback<Object>) connection -> Objects.requireNonNull(connection.info("memory")).get("used_memory"));
        List<String> totals = new ArrayList<>();
        totals.add(size);
        totals.add(String.valueOf(PhysicalUtils.getFreeMemory()));
        return WrapperDTO.success(totals);
    }

    @Override
    public WrapperDTO<String> clear(String tag) {
        if (StringUtils.isEmpty(tag)) {
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.flushDb();
                return "ok";
            });
            Set<String> keys = redisTemplate.keys("*");
            redisTemplate.delete(keys);
            return WrapperDTO.success();
        }
        String token = requestUtils.getCurrentToken();
        if (token.equalsIgnoreCase(tag)) {
            vOps.getOperations().delete(token);
        } else {
            Set<String> keys = redisTemplate.keys("*" + tag);
            redisTemplate.delete(keys);
        }
        return WrapperDTO.success();
    }
}
