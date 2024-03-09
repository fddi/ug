package top.ulug.core.deploy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.ulug.base.inf.TaskService;

/**
 * Created by liujf on 2024/3/9.
 * 逝者如斯夫 不舍昼夜
 */
@Service("TestServiceImpl")
public class TestServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TestServiceImpl.class);

    @Override
    public void execute() throws Exception {
        LOG.info("testService task running");
    }
}
