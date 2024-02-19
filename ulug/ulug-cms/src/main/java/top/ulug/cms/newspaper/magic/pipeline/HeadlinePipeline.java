package top.ulug.cms.newspaper.magic.pipeline;

import top.ulug.cms.newspaper.domain.PaperHeadline;
import top.ulug.cms.newspaper.repository.PaperHeadlineRepository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
public class HeadlinePipeline implements Pipeline {
    private final PaperHeadlineRepository headlineRepository;

    public HeadlinePipeline(PaperHeadlineRepository headlineRepository) {
        this.headlineRepository = headlineRepository;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<PaperHeadline> list = resultItems.get("list");
        if (list != null && list.size() > 0) {
            headlineRepository.saveAll(list);
        }
    }
}
