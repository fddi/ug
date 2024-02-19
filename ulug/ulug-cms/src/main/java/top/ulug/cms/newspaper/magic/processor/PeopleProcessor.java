package top.ulug.cms.newspaper.magic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.cms.football.magic.processor.BaseProcessor;
import top.ulug.cms.newspaper.domain.PaperHeadline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liujf on 2020/10/7.
 * 逝者如斯夫 不舍昼夜
 */
public class PeopleProcessor extends BaseProcessor {
    private final Logger logger = LoggerFactory.getLogger(PeopleProcessor.class);
    private final String url;
    private boolean process01 = false;
    private String dateItem;

    public PeopleProcessor(String url, String dateItem) {
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
        } else if (page.getUrl().toString().contains("/nw.")) {
            String content = page.getHtml().xpath("//div[@id='ozoom']").toString();
            page.putField("content", content);
            page.putField("link", page.getUrl().toString());
            page.putField("dateItem", dateItem);
        } else {
            if (!process01) {
                List<String> links = page.getHtml().xpath("//a[@id='pageLink']").links().all();
                process01 = true;
                page.addTargetRequests(links);
            }
            List<String> links = page.getHtml().xpath("//ul[@class='news-list']").links().all();
            List<String> titles = page.getHtml().xpath("//ul[@class='news-list']//a/text()").all();
            String layout = page.getHtml().xpath("//p[@class='left ban']/text()").toString();
            List<PaperHeadline> list = new ArrayList<>();
            for (int i = 0; i < links.size(); i++) {
                PaperHeadline headline = new PaperHeadline();
                headline.setDateItem(dateItem);
                headline.setType("1");
                headline.setLayout(layout);
                headline.setTitle(titles.get(i));
                headline.setLink(links.get(i));
                headline.setStatus("1");
                list.add(headline);
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
        url = url + "/rmrb/html/" + DateTimeUtils.getDateTime(date, "yyyy-MM") + "/"
                + DateTimeUtils.getDateTime(date, "dd")
                + "/nbs.D110000renmrb_01.htm";
        return url;
    }

    private String processingDateItem(String dateLeft) {
        if (dateLeft != null) {
            try {
                dateLeft = dateLeft.replaceAll(" ", "");
                dateLeft = dateLeft.replaceAll("人民日报", "");
                if (dateLeft.length() >= 11) {
                    dateLeft = dateLeft.substring(0, 11);
                    dateLeft = DateTimeUtils.getDateTime(
                            DateTimeUtils.getDate(dateLeft, "yyyy年MM月dd日"), "yyyyMMdd");
                    this.dateItem = dateLeft;
                }
            } catch (Exception e) {
                e.printStackTrace();
                dateLeft = DateTimeUtils.getDateTime("yyyyMMdd");
            }
        }
        return dateLeft;
    }
}
