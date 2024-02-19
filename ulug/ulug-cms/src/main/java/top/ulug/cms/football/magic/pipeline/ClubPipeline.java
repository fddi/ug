package top.ulug.cms.football.magic.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.base.security.Base64;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.football.domain.BallClub;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.repository.BallClubRepository;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.repository.MediaImageRepository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;

/**
 * Created by fddiljf on 2017/3/21.
 * 逝者如斯夫 不舍昼夜
 */
public class ClubPipeline implements Pipeline {
    private BallClubRepository clubRep;
    private MediaImageRepository imageRep;
    private BallCountry country;
    Logger logger = LoggerFactory.getLogger(ClubPipeline.class);


    public ClubPipeline(BallCountry country, MediaImageRepository imageRep, BallClubRepository clubRep) {
        this.country = country;
        this.clubRep = clubRep;
        this.imageRep = imageRep;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = resultItems.getRequest().getUrl();
        logger.info("get page: {}", url);
        if (!url.contains("/team/")) {
            return;
        }
        String magicCode = url.substring(0, url.length() - 1);
        magicCode = magicCode.substring(magicCode.lastIndexOf("/"));
        String name = (String) resultItems.getAll().get("name");
        String enName = (String) resultItems.getAll().get("enName");
        String img = (String) resultItems.getAll().get("img");
        String buildTime = (String) resultItems.getAll().get("buildTime");
        String stadium = (String) resultItems.getAll().get("stadium");
        String city = (String) resultItems.getAll().get("city");
        BallClub club = clubRep.findByClubName(name);
        if (club != null) {
            return;
        }
        club = new BallClub();
        club.setCountry(country);
        club.setClubName(name);
        club.setEnName(enName);
        club.setBuildTime(buildTime);
        club.setStadium(stadium);
        club.setCity(city);
        club.setMagicCode(magicCode);
        final String flagType = img.substring(img.lastIndexOf("."));
        try {
            URL openUrl = new URL(img);
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
            image.setImgType(flagType);
            String encodeData = Base64.encodeBytes(outputStream.toByteArray());
            image.setImgData(encodeData);
            image.setImgTags("club");
            String key = StringUtils.createFileKey("ball", "club");
            image.setImgKey(key);
            imageRep.save(image);
            club.setFlag(key);
            clubRep.save(club);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}
