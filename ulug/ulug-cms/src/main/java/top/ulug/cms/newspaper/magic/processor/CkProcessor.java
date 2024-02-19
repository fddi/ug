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
public class CkProcessor extends BaseProcessor {
    private final Logger LOG = LoggerFactory.getLogger(CkProcessor.class);
    private final String url;
    private boolean process01 = false;
    private int n = 0;
    private String dateItem;

    public CkProcessor(String url, String dateItem) {
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
            String content = page.getHtml().xpath("//founder-content").toString();
            page.putField("content", content);
            page.putField("link", page.getUrl().toString());
            page.putField("dateItem", dateItem);
        } else {
            if (!process01) {
                List<String> links = page.getHtml().xpath("//div[@id='bmdh']").links().all();
                process01 = true;
                page.addTargetRequests(links);
            }
            List<String> layouts = page.getHtml().xpath("//div[@id='bmdh']//a/text()").all();
            List<String> links = page.getHtml().xpath("//ul[@class='ul02_l']").links().all();
            List<String> titles = page.getHtml().xpath("//ul[@class='ul02_l']//div/text()").all();
            String layout = layouts.size() > n ? layouts.get(n) : layouts.get(layouts.size() - 1);
            n++;
            List<PaperHeadline> list = new ArrayList<>();
            for (int i = 0; i < links.size(); i++) {
                PaperHeadline headline = new PaperHeadline();
                headline.setDateItem(dateItem);
                headline.setType("3");
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
        url = url + "/html/" + DateTimeUtils.getDateTime(date, "yyyy-MM") + "/"
                + DateTimeUtils.getDateTime(date, "dd")
                + "/node_2.htm";
        return url;
    }

}
