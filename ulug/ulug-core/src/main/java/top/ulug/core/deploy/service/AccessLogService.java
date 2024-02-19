package top.ulug.core.deploy.service;

/**
 * Created by liujf on 2020-09-11.
 * 逝者如斯夫 不舍昼夜
 */
public interface AccessLogService {

    /**
     * @param appId
     * @param uri
     */
    void writeBase(String appId, String pageId, String cid, String uri);
}
