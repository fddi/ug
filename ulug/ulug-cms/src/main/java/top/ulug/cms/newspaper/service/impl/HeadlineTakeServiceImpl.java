package top.ulug.cms.newspaper.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.cms.media.domain.MediaContent;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.repository.MediaContentRepository;
import top.ulug.cms.media.repository.MediaSubjectRepository;
import top.ulug.cms.newspaper.domain.PaperContent;
import top.ulug.cms.newspaper.domain.PaperHeadline;
import top.ulug.cms.newspaper.magic.pipeline.ContentPipeline;
import top.ulug.cms.newspaper.magic.pipeline.HeadlinePipeline;
import top.ulug.cms.newspaper.magic.processor.CkProcessor;
import top.ulug.cms.newspaper.magic.processor.JingJi21Processor;
import top.ulug.cms.newspaper.magic.processor.PeopleProcessor;
import top.ulug.cms.newspaper.magic.processor.SdProcessor;
import top.ulug.cms.newspaper.repository.PaperContentRepository;
import top.ulug.cms.newspaper.repository.PaperHeadlineRepository;
import top.ulug.cms.newspaper.service.HeadlineTakeService;
import ug.template.engine.core.launcher.CombExecutor;
import us.codecraft.webmagic.Spider;

import java.util.*;

/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class HeadlineTakeServiceImpl implements HeadlineTakeService {

    private final Logger logger = LoggerFactory.getLogger(HeadlineTakeServiceImpl.class);
    @Autowired
    PaperHeadlineRepository headlineRepository;
    @Autowired
    PaperContentRepository paperContentRepository;
    private final static String URL_PEOPLE = "http://paper.people.com.cn";
    private final static String URL_21 = "http://epaper.21jingji.com";
    private final static String URL_CK = "http://dz.jjckb.cn/www/pages/webpage2009";
    private final static String URL_SD = "http://epaper.zqrb.cn";
    private final String[] names = {"人民日报", "21世纪经济报道", "经济参考报", "证券日报"};

    @Autowired
    MediaContentRepository contentRepository;
    @Autowired
    MediaSubjectRepository subjectRepository;
    @Autowired
    CombExecutor combExecutor;

    @Override
    public void runAll(String dateItem) {
        paperContentRepository.deleteByDateItem(dateItem);
        headlineRepository.deleteByDateItem(dateItem);
        this.runPeopleData(dateItem);
        this.runSdData(dateItem);
        this.runCkData(dateItem);
    }

    @Override
    public void runPeopleData(String dateItem) {
        Spider.create(new PeopleProcessor(URL_PEOPLE, dateItem))
                .addPipeline(new HeadlinePipeline(headlineRepository))
                .addPipeline(new ContentPipeline(headlineRepository, paperContentRepository))
                .addUrl(URL_PEOPLE)
                .thread(2).start();
    }

    @Override
    public void run21Data(String dateItem) {
        Spider.create(new JingJi21Processor(URL_21, dateItem))
                .addPipeline(new HeadlinePipeline(headlineRepository))
                .addPipeline(new ContentPipeline(headlineRepository, paperContentRepository))
                .addUrl(URL_21)
                .thread(2).start();
    }

    @Override
    public void runSdData(String dateItem) {
        Spider.create(new SdProcessor(URL_SD, dateItem))
                .addPipeline(new HeadlinePipeline(headlineRepository))
                .addPipeline(new ContentPipeline(headlineRepository, paperContentRepository))
                .addUrl(URL_SD)
                .thread(2).start();
    }

    @Override
    public void runCkData(String dateItem) {
        Spider.create(new CkProcessor(URL_CK, dateItem))
                .addPipeline(new HeadlinePipeline(headlineRepository))
                .addPipeline(new ContentPipeline(headlineRepository, paperContentRepository))
                .addUrl(URL_CK)
                .thread(1).start();
    }

    @Override
    public void createArticle(String dateItem) throws Exception {
        List<PaperHeadline> list = headlineRepository.findByDateItem(dateItem);
        if (list == null || list.size() == 0) {
            return;
        }
        String type = "-1";
        String layout = null;
        String paperName;
        List<PaperHeadline> ttList = new ArrayList<>();
        String ttLayout = null;
        StringBuilder htmlUl = new StringBuilder();
        StringBuilder htmlTT = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        HashMap<String, Object> params;
        for (int i = 0; i < list.size(); i++) {
            PaperHeadline headline = list.get(i);
            if (!type.equals(headline.getType())) {
                summary.append(headline.getTitle()).append("；");
                type = headline.getType();
                layout = headline.getLayout();
                ttLayout = headline.getLayout();
                ttList.add(headline);
                if (i > 0) {
                    htmlUl.append("</tbody></table>");
                }
                paperName = names[Integer.parseInt(type) - 1];
                params = new HashMap<>();
                params.put("title", paperName);
                htmlUl.append(combExecutor.getComb("paper.paperTitle", params));
                htmlUl.append("<table><tbody>");
                params = new HashMap<>();
                params.put("layout", layout);
                htmlUl.append(combExecutor.getComb("paper.paperLayout", params));
            } else if (!Objects.equals(layout, headline.getLayout())) {
                layout = headline.getLayout();
                params = new HashMap<>();
                params.put("layout", layout);
                htmlUl.append(combExecutor.getComb("paper.paperLayout", params));
            } else if (Objects.equals(ttLayout, headline.getLayout())) {
                ttList.add(headline);
            }
            params = new HashMap<>();
            params.put("link", headline.getLink());
            params.put("title", headline.getTitle());
            htmlUl.append(combExecutor.getComb("paper.paperLayoutLine", params));
        }
        htmlUl.append("</tbody></table>");
        type = ttList.get(0).getType();
        params = new HashMap<>();
        paperName = names[Integer.parseInt(type) - 1];
        params.put("title", paperName);
        htmlTT.append(combExecutor.getComb("paper.paperTitle", params));
        htmlTT.append("<ol>");
        for (PaperHeadline headline : ttList) {
            if (!type.equals(headline.getType())) {
                htmlTT.append("</ol>");
                type = headline.getType();
                params = new HashMap<>();
                paperName = names[Integer.parseInt(type) - 1];
                params.put("title", paperName);
                htmlTT.append(combExecutor.getComb("paper.paperTitle", params));
                htmlTT.append("<ol>");
            }
            params = new HashMap<>();
            params.put("link", headline.getLink());
            params.put("title", headline.getTitle());
            htmlTT.append(combExecutor.getComb("paper.paperLine", params));
            PaperContent paperContent = paperContentRepository.findByLineId(headline.getLineId());
            if (paperContent != null) {
                htmlTT.append("<div style='border:1px dashed #cccccc;margin:5px;padding:5px;'>");
                htmlTT.append(paperContent.getPaper());
                htmlTT.append("</div>");
            }
        }
        htmlTT.append("</ol>");
        Date date = DateTimeUtils.getDate(dateItem, "yyyyMMdd");
        MediaContent content = new MediaContent();
        MediaSubject subject = subjectRepository.findBySubjectCode("ugsdb");
        String contentTitle = String.format("%s报刊导航",
                DateTimeUtils.getDateTime(date, "yyyy年MM月dd日"));
        content.setSubject(subject);
        content.setContentTitle(contentTitle);
        content.setSigner(subject.getSubjectName());
        content.setSummary(summary.toString());
        content.setArticle(htmlTT.append(htmlUl).toString());
        content.setTags("每日读报,新闻");
        content.setPublishStatus("1");
        content.setUnitCode(subject.getUnitCode());
        content.setAreaCode(subject.getAreaCode());
        content.setSort(1);
        contentRepository.save(content);
        logger.info("文章自动生成：" + contentTitle);
    }

}
