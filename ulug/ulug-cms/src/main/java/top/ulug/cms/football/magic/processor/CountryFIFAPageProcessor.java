package top.ulug.cms.football.magic.processor;

import top.ulug.base.security.Base64;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.repository.BallCountryRepository;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.repository.MediaImageRepository;
import us.codecraft.webmagic.Page;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by fddiljf on 2017/3/20.
 * 逝者如斯夫 不舍昼夜
 */
public class CountryFIFAPageProcessor extends BaseProcessor {

    private BallCountryRepository countryRep;
    private MediaImageRepository imageRep;

    private String flagPath;

    public CountryFIFAPageProcessor(BallCountryRepository countryRep, MediaImageRepository imageRep) {
        this.countryRep = countryRep;
        this.imageRep = imageRep;
    }

    public void process(Page page) {
        List<String> enNames = page.getHtml().xpath("//span[@class='fi-t__nText']/text()").all();
        List<String> rankings = page.getHtml().xpath("//td[@class='fi-table__td fi-table__rank']/span/text()").all();
        List<String> points = page.getHtml().xpath("//td[@class='fi-table__td fi-table__points']/span/text()").all();
        List<String> images = page.getHtml().xpath("//div[@class='fi-t__i ']/img/@src").all();
        this.updateAll(enNames, rankings, points, images);
    }

    private void updateAll(List<String> enNames, List<String> rankings, List<String> points, List<String> images) {
        if (enNames == null || rankings == null || points == null || images == null) return;
        if (enNames.size() != rankings.size() || enNames.size() != points.size() || enNames.size() != images.size()) {
            logger.error("排名与国家获取失败！");
            return;
        }
        for (int i = 0; i < enNames.size(); i++) {
            logger.info("enNames:" + enNames.get(i));
            BallCountry country = countryRep.findByEnName(enNames.get(i));
            if (country != null && country.getFlag() == null) {
                country.setNowRanking(Integer.parseInt(rankings.get(i)));
                country.setNowPoints(Integer.parseInt(points.get(i)));
                final String flagUrl = images.get(i);
                final String extension = ".png";
                try {
                    URL openUrl = new URL(flagUrl);
                    DataInputStream inputStream = new DataInputStream(openUrl.openStream());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.close();
                    MediaImage image = new MediaImage();
                    image.setImgType(extension);
                    String encodeData = Base64.encodeBytes(outputStream.toByteArray());
                    image.setImgData(encodeData);
                    image.setImgTags("country");
                    String key = StringUtils.createFileKey("ball", "country");
                    image.setImgKey(key);
                    imageRep.save(image);
                    country.setFlag(key);
                    countryRep.save(country);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.toString());
                }
            }
        }
    }
}
