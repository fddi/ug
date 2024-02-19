package top.ulug.cms.football.magic.processor;

import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

/**
 * Created by fddiljf on 2017/3/20.
 * 逝者如斯夫 不舍昼夜
 */

@Service
public class CountryCaiDetailProcessor extends BaseProcessor {

    public void process(Page page) {
        if (page.getUrl().regex("http://liansai.500.com/paiming/").match()) {
            page.addTargetRequests(page.getHtml().links().regex("(http://liansai\\.500\\.com/team/\\w+/)").all());
        } else {
            String name = page.getHtml().xpath("//div[@class='itm_tit']/text()").toString();
            String enName = page.getHtml().xpath("//div[@class='itm_name_en']/text()").toString();
            if (name != null && enName != null) {
                page.putField("name", name.trim());
                page.putField("enName", enName.trim());
            }
        }
    }
}
