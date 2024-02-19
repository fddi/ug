package top.ulug.cms.newspaper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.inf.TaskService;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.cms.newspaper.service.HeadlineTakeService;

import java.util.Date;

/**
 * Created by liujf on 2022/7/21.
 * 逝者如斯夫 不舍昼夜
 */
@Service("PaperTaskServiceImpl")
public class PaperTaskServiceImpl implements TaskService {
    @Autowired
    HeadlineTakeService headlineTakeService;

    @Override
    public void execute() {
        String dateItem = DateTimeUtils.getDateTime(new Date(), "yyyyMMdd");
        headlineTakeService.runAll(dateItem);
    }
}
