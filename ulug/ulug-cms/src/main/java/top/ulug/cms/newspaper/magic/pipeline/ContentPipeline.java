package top.ulug.cms.newspaper.magic.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.newspaper.domain.PaperContent;
import top.ulug.cms.newspaper.domain.PaperHeadline;
import top.ulug.cms.newspaper.repository.PaperContentRepository;
import top.ulug.cms.newspaper.repository.PaperHeadlineRepository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;


/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
public class ContentPipeline implements Pipeline {
    private final PaperHeadlineRepository headlineRepository;
    private final PaperContentRepository contentRepository;

    private final Logger LOG = LoggerFactory.getLogger(ContentPipeline.class);

    public ContentPipeline(PaperHeadlineRepository headlineRepository, PaperContentRepository contentRepository) {
        this.headlineRepository = headlineRepository;
        this.contentRepository = contentRepository;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String content = resultItems.get("content");
        if (!StringUtils.isEmpty(content)) {
            String link = resultItems.get("link");
            PaperHeadline headline = headlineRepository.findByLink(link);
            if (headline != null) {
                LOG.info("保存详细信息该链接[{}]详细信息。", link);
                PaperContent pc = new PaperContent();
                pc.setHeadline(headline);
                pc.setLink(link);
                pc.setPaper(content);
                pc.setDateItem(resultItems.get("dateItem"));
                contentRepository.save(pc);
            }
        }
    }
}
