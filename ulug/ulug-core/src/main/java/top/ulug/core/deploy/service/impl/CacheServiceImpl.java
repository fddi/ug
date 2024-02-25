package top.ulug.core.deploy.service.impl;

import jakarta.annotation.Resource;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.deploy.domain.DeployClient;
import top.ulug.core.deploy.service.CacheService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liu
 * @Date 2024/2/23 10:30 星期五
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Resource
    CacheManager cacheManager;

    @Override
    public List<String> getTokens(String appId, String userName) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            return cache.get(userName, ArrayList.class);
        }
        return null;
    }

    @Override
    public void cacheTokens(String appId, String userName, String token) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            List<String> tokens = cache.get(userName, ArrayList.class);
            if (tokens == null) {
                tokens = new ArrayList<>();
            }
            tokens.add(token);
            cache.put(userName, tokens);
        }
    }

    @Override
    public AuthDTO getAuth(String appId, String token) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            return cache.get(token, AuthDTO.class);
        }
        return null;
    }

    @Override
    public void cacheAuth(String appId, String token, AuthDTO dto) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            cache.put(token, dto);
        }
    }

    @Override
    public DeployClient getClient(String appId) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            return cache.get(appId, DeployClient.class);
        }
        return null;
    }

    @Override
    public void cacheClient(String appId, DeployClient client) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            cache.put(appId, client);
        }
    }

    @Override
    public String getTag(String appId, String tag) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            return cache.get(tag, String.class);
        }
        return null;
    }

    @Override
    public void cacheTag(String appId, String tag, String value) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            cache.put(tag, value);
        }
    }

    @Override
    public void clearCache(String appId, String tag) {
        Cache cache = cacheManager.getCache(appId);
        if (cache != null) {
            cache.evictIfPresent(tag);
            if ("0".equals(tag)) cache.clear();
        }
    }
}
