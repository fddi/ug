package top.ulug.cms.football.magic.processor;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import top.ulug.cms.football.domain.BallCountry;
import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fddiljf on 2017/3/20.
 * 逝者如斯夫 不舍昼夜
 */

@Service
public class CountryCaiPageProcessor extends BaseProcessor {

    public void process(Page page) {
        List<BallCountry> list = new ArrayList<>();
        List<String> c2 = page.getHtml().xpath("//tr[@class='pm_-2']/td[@class='td_name']/a/text()").all();
        List<String> v2 = page.getHtml().xpath("//tr[@class='pm_-2']/td[@class='td_paim']/text()").all();
        this.addCountry(list, c2, v2, "欧洲");
        List<String> c3 = page.getHtml().xpath("//tr[@class='pm_-3']/td[@class='td_name']/a/text()").all();
        List<String> v3 = page.getHtml().xpath("//tr[@class='pm_-3']/td[@class='td_paim']/text()").all();
        this.addCountry(list, c3, v3, "美洲");
        List<String> c4 = page.getHtml().xpath("//tr[@class='pm_-4']/td[@class='td_name']/a/text()").all();
        List<String> v4 = page.getHtml().xpath("//tr[@class='pm_-4']/td[@class='td_paim']/text()").all();
        this.addCountry(list, c4, v4, "亚洲");
        List<String> c5 = page.getHtml().xpath("//tr[@class='pm_-5']/td[@class='td_name']/a/text()").all();
        List<String> v5 = page.getHtml().xpath("//tr[@class='pm_-5']/td[@class='td_paim']/text()").all();
        this.addCountry(list, c5, v5, "非洲");
        List<String> c6 = page.getHtml().xpath("//tr[@class='pm_-6']/td[@class='td_name']/a/text()").all();
        List<String> v6 = page.getHtml().xpath("//tr[@class='pm_-6']/td[@class='td_paim']/text()").all();
        this.addCountry(list, c6, v6, "大洋洲");
        page.putField("list", JSON.toJSONString(list));
    }

    private void addCountry(List<BallCountry> list, List<String> v, List<String> vv, String continent) {
        if (list == null || v == null || v.size() == 0 || vv == null || vv.size() == 0) return;
        if (v.size() != vv.size()) {
            logger.error("{}:排名与国家获取失败！", continent);
            return;
        }
        for (int i = 0; i < v.size(); i++) {
            BallCountry country = new BallCountry();
            country.setCountryName(v.get(i));
            country.setContinent(continent);
            country.setNowRanking(Integer.parseInt(vv.get(i)));
            list.add(country);
        }
    }

}
