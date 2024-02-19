package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.ulug.core.deploy.domain.DeployAccessLog;
import top.ulug.core.deploy.repository.DeployAccessLogRepository;
import top.ulug.core.deploy.service.AccessLogService;

/**
 * Created by liujf on 2020-09-11.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {
    @Autowired
    DeployAccessLogRepository accessLogRepository;

    @Override
    @Async
    public void writeBase(String appId, String pageId, String cid, String uri) {
        try {
            DeployAccessLog accessLog = new DeployAccessLog();
            accessLog.setClientName(appId);
            accessLog.setPageId(pageId);
            accessLog.setCid(cid);
            accessLog.setUri(uri);
            accessLogRepository.save(accessLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
