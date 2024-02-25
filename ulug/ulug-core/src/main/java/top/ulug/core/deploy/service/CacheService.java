package top.ulug.core.deploy.service;

import top.ulug.base.dto.TreeDTO;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.dto.OrgDTO;
import top.ulug.core.deploy.domain.DeployAbility;
import top.ulug.core.deploy.domain.DeployClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liu
 * @Date 2024/2/23 9:27 星期五
 */
public interface CacheService {
    /**
     * 获取用户token列表
     *
     * @param appId    终端应用
     * @param userName 用户名
     * @return token列表
     */
    List<String> getTokens(String appId, String userName);

    /**
     * 添加用户token列表
     *
     * @param appId    终端应用
     * @param userName 用户名
     * @param token    token列表
     */
    void cacheTokens(String appId, String userName, String token);

    /**
     * 获取认证用户信息
     *
     * @param appId 终端应用
     * @param token 令牌
     * @return 认证用户信息
     */
    AuthDTO getAuth(String appId, String token);

    /**
     * 缓存认证用户信息
     *
     * @param appId 终端应用
     * @param token 令牌
     * @param dto   认证用户信息
     */
    void cacheAuth(String appId, String token, AuthDTO dto);

    /**
     * 获取终端信息
     *
     * @param appId 终端应用ID
     * @return 终端应用信息
     */
    DeployClient getClient(String appId);

    /**
     * 缓存终端信息
     *
     * @param appId  终端应用ID
     * @param client 终端应用信息
     */
    void cacheClient(String appId, DeployClient client);

    /**
     * 获取缓存信息
     *
     * @param appId 终端ID
     * @param tag   缓存名称
     * @return 字符串值
     */
    String getTag(String appId, String tag);

    /**
     * 缓存保存
     *
     * @param appId 终端ID
     * @param tag   缓存名称
     * @param value 值
     */
    void cacheTag(String appId, String tag, String value);

    /**
     * 清理缓存
     *
     * @param appId 终端ID
     * @param tag   缓存名称
     */
    void clearCache(String appId, String tag);
}
