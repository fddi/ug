package top.ulug.cms.football.magic.processor;

import top.ulug.cms.football.repository.BallPlayerRepository;
import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fddiljf on 2017/3/23.
 * 逝者如斯夫 不舍昼夜
 */
public class PlayerPageProcessor extends BaseProcessor {
    private String countryName;
    private String url;
    private BallPlayerRepository playerRep;

    public PlayerPageProcessor(String countryName, String url, BallPlayerRepository playerRep) {
        this.countryName = countryName;
        this.url = url;
        this.playerRep = playerRep;
    }

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(url).match()) {
            List<String> links = page.getHtml().xpath("//tbody[@id='hot_score_tbody']").links().regex("(/team/\\w+/)").all();
            List<String> clinks = new ArrayList<>();
            for (String link : links) {
                clinks.add("http://liansai.500.com" + link);
            }
            page.addTargetRequests(clinks);
        }
        if (page.getUrl().toString().contains("team")) {
            String name = page.getHtml().xpath("//div[@class='itm_tit']/text()").toString();
            if (name != null) {
                long size = playerRep.findCountByClubName(name);
                if (size <= 0) {
                    List<String> links = page.getHtml().xpath("//div[@class='lqiudzr_r lqiudmd']").links().all();
                    page.addTargetRequests(links);
                }
            }
        } else {
            String playerName = page.getHtml().xpath("//div[@class='itm_name']/text()").toString();
            String enName = page.getHtml().xpath("//div[@class='itm_name_en']/text()").toString();
            String img = page.getHtml().xpath("//div[@class='itm_img']/img/@src").toString();
            List<String> bd = page.getHtml().xpath("//div[@class='itm_bd']//td/text()").all();
            if (playerName == null || bd == null || bd.size() < 11) {
                return;
            }
            try {
                String countryName = bd.get(1).replaceAll("国籍：", "").trim();
                String playerValue = bd.get(2).replaceAll("球员身价：", "").trim();
                String birthday = bd.get(3).replaceAll("生日：", "").trim();
                String clubName = bd.get(4).replaceAll("目前效力：", "").trim();
                String tall = bd.get(6).replaceAll("身高：", "").trim();
                String position = bd.get(7).replaceAll("位置：", "").trim();
                String weight = bd.get(9).replaceAll("体重：", "").trim();
                String clubNumber = bd.get(10).replaceAll("球衣号码：", "").trim();
                page.putField("playerName", playerName);
                page.putField("enName", enName);
                page.putField("countryName", countryName);
                page.putField("birthday", birthday);
                page.putField("clubName", clubName);
                page.putField("position", position);
                page.putField("tall", tall);
                page.putField("weight", weight);
                page.putField("clubNumber", clubNumber);
                page.putField("playerValue", playerValue);
                page.putField("img", img);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

        }
    }

}
