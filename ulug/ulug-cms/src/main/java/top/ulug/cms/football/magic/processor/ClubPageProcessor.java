package top.ulug.cms.football.magic.processor;

import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fddiljf on 2017/3/23.
 * 逝者如斯夫 不舍昼夜
 */
public class ClubPageProcessor extends BaseProcessor {
    public ClubPageProcessor() {
    }

    @Override
    public void process(Page page) {
        if (page.getUrl().toString().contains("jifen")) {
            List<String> links = page.getHtml().xpath("//tbody[@id='hot_score_tbody']").links().regex("(/team/\\w+/)").all();
            List<String> clinks = new ArrayList<>();
            for (String link : links) {
                clinks.add("http://liansai.500.com" + link);
            }
            page.addTargetRequests(clinks);
        } else if (page.getUrl().toString().contains("teams")) {
            List<String> links = page.getHtml().xpath("//tbody[@class='jTrInterval data_t']").links().regex("(/team/\\w+/)").all();
            page.addTargetRequests(links);
        } else {
            String name = page.getHtml().xpath("//div[@class='itm_tit']/text()").toString();
            String enName = page.getHtml().xpath("//div[@class='itm_name_en']/text()").toString();
            String img = page.getHtml().xpath("//div[@class='itm_logo']/img/@src").toString();
            String buildTime = page.getHtml().xpath("//div[@class='itm_bd']/table/tbody/tr[1]/td[1]/text()").toString();
            buildTime = buildTime.replace("成立时间：", "");
            String stadium = page.getHtml().xpath("//div[@class='itm_bd']/table/tbody/tr[2]/td[2]/text()").toString();
            stadium = stadium.replace("球场：", "");
            String city = page.getHtml().xpath("//div[@class='itm_bd']/table/tbody/tr[3]/td[1]/text()").toString();
            city = city.replace("所在城市：", "");
            if (name != null && enName != null) {
                page.putField("name", name.trim());
                page.putField("enName", enName.trim());
                page.putField("img", img.trim());
                page.putField("buildTime", buildTime.trim());
                page.putField("stadium", stadium.trim());
                page.putField("city", city.trim());
            }
        }
    }

}
