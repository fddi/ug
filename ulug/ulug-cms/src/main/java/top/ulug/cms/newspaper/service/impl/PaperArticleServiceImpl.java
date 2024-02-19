package top.ulug.cms.newspaper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.inf.TaskService;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.cms.newspaper.service.HeadlineTakeService;

import java.util.Date;

/**
 * Created by liujf on 2022/7/23.
 * 逝者如斯夫 不舍昼夜
 */
@Service("PaperArticleServiceImpl")
public class PaperArticleServiceImpl  implements TaskService {
    @Autowired
    HeadlineTakeService headlineTakeService;

    @Override
    public void execute() throws Exception {
        String dateItem = DateTimeUtils.getDateTime(new Date(), "yyyyMMdd");
        headlineTakeService.createArticle(dateItem);
    }
}
