package top.ulug.cms.newspaper.magic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.football.magic.processor.BaseProcessor;
import top.ulug.cms.newspaper.domain.PaperHeadline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liujf on 2020/10/9.
 * 逝者如斯夫 不舍昼夜
 */
public class JingJi21Processor extends BaseProcessor {
    private final Logger logger = LoggerFactory.getLogger(JingJi21Processor.class);
    private final String url;
    private String dateItem;

    public JingJi21Processor(String url, String dateItem) {
        this.url = url;
        this.dateItem = dateItem;
    }

    @Override
    public void process(Page page) {
        super.process(page);
        if (page.getUrl().toString().equals(url)) {
            Date date = DateTimeUtils.getDate(dateItem, "yyyyMMdd");
            if (date == null) {
                return;
            }
            page.addTargetRequest(processingUrl(url, dateItem));
        } else if (page.getUrl().toString().contains("/article/")) {
            String content = page.getHtml().xpath("//div[@class='textContent']").toString();
            page.putField("content", content);
            page.putField("link", page.getUrl().toString());
            page.putField("dateItem", dateItem);
        } else {
            List<String> links = page.getHtml().xpath("//div[@class='main']").links().all();
            List<String> titles = page.getHtml().xpath("//div[@class='main']//a/text()").all();
            List<String> layouts = page.getHtml().xpath("//div[@class='main']/a/h5/text()").all();
            List<PaperHeadline> list = new ArrayList<>();
            if (links == null || titles == null || layouts == null || layouts.size() == 0) {
                return;
            }
            int n = 0;
            String layout = layouts.get(n);
            for (int i = 1; i < links.size(); i++) {
                String link = links.get(i);
                String title = titles.get(i);
                if (StringUtils.isEmpty(link) && n < (layouts.size() - 1)) {
                    n++;
                    layout = layouts.get(n);
                    continue;
                }
                if (!StringUtils.isEmpty(title)) {
                    PaperHeadline headline = new PaperHeadline();
                    headline.setDateItem(dateItem);
                    headline.setType("2");
                    headline.setLayout(layout);
                    headline.setTitle(title);
                    headline.setLink(link);
                    headline.setStatus("1");
                    list.add(headline);
                }
            }
            page.putField("list", list);
            //内容抓取
            page.addTargetRequests(links);
        }
    }

    @Override
    public Site getSite() {
        return super.getSite().setCharset("utf-8");
    }

    private String processingUrl(String url, String dateItem) {
        Date date = DateTimeUtils.getDate(dateItem, "yyyyMMdd");
        return url + "/?appDate=" + DateTimeUtils.getDateTime(date, "yyyy-MM-dd");
    }
}
