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
 * Created by liujf on 2020/10/11.
 * 逝者如斯夫 不舍昼夜
 */
public class SdProcessor extends BaseProcessor {
    private final Logger logger = LoggerFactory.getLogger(SdProcessor.class);
    private final String url;
    private String dateItem;

    public SdProcessor(String url, String dateItem) {
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
        } else if (page.getUrl().toString().contains("content_")) {
            String content = page.getHtml().xpath("//div[@class='neiyee']").toString();
            page.putField("content", content);
            page.putField("link", page.getUrl().toString());
            page.putField("dateItem", dateItem);
        } else {
            List<String> links = page.getHtml().xpath("//div[@class='neir']").links().all();
            List<String> cLinks = new ArrayList<>();
            List<String> titles = page.getHtml().xpath("//div[@class='neir']//a/text()").all();
            List<String> tLinks = page.getHtml().xpath("//div[@class='neir']//h2/a").links().all();
            List<String> layouts = page.getHtml().xpath("//div[@class='neir']//h2/a/text()").all();
            List<PaperHeadline> list = new ArrayList<>();
            if (links == null || titles == null || layouts == null || layouts.size() == 0) {
                return;
            }
            int n = 0;
            String layout = layouts.get(n);
            String tLink = tLinks.get(n + 1);
            for (int i = 1; i < links.size(); i++) {
                String link = links.get(i);
                String title = titles.get(i);
                if (link != null && link.equals(tLink) && n < (layouts.size() - 1)) {
                    n++;
                    layout = layouts.get(n);
                    tLink = tLinks.get(n + 1);
                    continue;
                }
                if (layout.trim().indexOf("C") == 0 || layout.trim().indexOf("信息披露") > 0) {
                    break;
                }
                cLinks.add(link);
                if (!StringUtils.isEmpty(title)) {
                    PaperHeadline headline = new PaperHeadline();
                    headline.setDateItem(dateItem);
                    headline.setType("4");
                    headline.setLayout(layout);
                    headline.setTitle(title);
                    headline.setLink(link);
                    headline.setStatus("1");
                    list.add(headline);
                }
            }
            page.putField("list", list);
            //内容抓取
            page.addTargetRequests(cLinks);
        }
    }

    @Override
    public Site getSite() {
        return super.getSite().setCharset("utf-8");
    }

    private String processingUrl(String url, String dateItem) {
        Date date = DateTimeUtils.getDate(dateItem, "yyyyMMdd");
        url = url + "/html/" + DateTimeUtils.getDateTime(date, "yyyy-MM") + "/"
                + DateTimeUtils.getDateTime(date, "dd")
                + "/node_2.htm";
        return url;
    }
}
