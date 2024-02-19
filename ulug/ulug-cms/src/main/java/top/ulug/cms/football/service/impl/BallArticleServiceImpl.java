package top.ulug.cms.football.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.inf.TaskService;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.cms.football.service.BallTakeService;

import java.util.Date;

/**
 * Created by liujf on 2022/7/25.
 * 逝者如斯夫 不舍昼夜
 */
@Service("BallArticleServiceImpl")
public class BallArticleServiceImpl implements TaskService {
    @Autowired
    BallTakeService ballTakeService;

    @Override
    public void execute() throws Exception {
        String dateItem = DateTimeUtils.getDateTime(new Date(), "yyyyMMdd");
        ballTakeService.createArticle(dateItem);
    }
}
