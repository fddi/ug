package top.ulug.cms.football.magic.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.base.security.Base64;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.football.domain.BallClub;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.domain.BallPlayer;
import top.ulug.cms.football.repository.BallClubRepository;
import top.ulug.cms.football.repository.BallCountryRepository;
import top.ulug.cms.football.repository.BallPlayerRepository;
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
public class PlayerPipeline implements Pipeline {
    Logger logger = LoggerFactory.getLogger(CountryCaiPagePipeline.class);
    private BallCountryRepository countryRep;
    private BallClubRepository clubRep;
    private BallPlayerRepository playerRep;
    private MediaImageRepository imageRep;


    public PlayerPipeline(MediaImageRepository imageRep, BallCountryRepository countryRep,
                          BallClubRepository clubRep,
                          BallPlayerRepository playerRep) {
        this.countryRep = countryRep;
        this.clubRep = clubRep;
        this.playerRep = playerRep;
        this.imageRep = imageRep;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = resultItems.getRequest().getUrl();
        logger.info("get page: {}", url);
        if (!url.contains("player")) {
            return;
        }
        String enName = (String) resultItems.getAll().get("enName");
        String playerName = (String) resultItems.getAll().get("playerName");
        if (enName == null && playerName == null) return;
        if (playerName == null || "".equals(playerName))
            playerName = enName;
        String countryName = (String) resultItems.getAll().get("countryName");
        BallCountry country = countryRep.findByCountryName(countryName);
        if (country == null) {
            logger.error("can not find country; name:{},country:{}", playerName, countryName);
        }
        String clubName = (String) resultItems.getAll().get("clubName");
        BallClub club = clubRep.findByClubName(clubName);
        if (club == null) {
            String magicCode = url.substring(0, url.length() - 1);
            magicCode = magicCode.substring(magicCode.lastIndexOf("/")).split("-")[0];
            club = clubRep.findFirstByMagicCode(magicCode);
            if (club == null) {
                logger.error("can not find club------ magicCode:{}, name:{},club:{}", magicCode, playerName, clubName);
            }
        }
        String birthday = (String) resultItems.getAll().get("birthday");
        String position = (String) resultItems.getAll().get("position");
        String weight = (String) resultItems.getAll().get("weight");
        String clubNumber = (String) resultItems.getAll().get("clubNumber");
        String tall = (String) resultItems.getAll().get("tall");

        BallPlayer player = new BallPlayer();
        player.setBirthday(birthday);
        if (club != null) player.setClub(club);
        if (country != null) player.setCountry(country);
        player.setEnName(enName);
        player.setPlayerName(playerName);
        player.setPosition(position);
        player.setWeight(weight);
        player.setClubNumber(clubNumber);
        player.setTall(tall);
        String img = (String) resultItems.getAll().get("img");
        final String avatarType = img.substring(img.lastIndexOf("."));
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
            image.setImgType(avatarType);
            String encodeData = Base64.encodeBytes(outputStream.toByteArray());
            image.setImgData(encodeData);
            image.setImgTags("ball-avatar");
            String key = StringUtils.createFileKey("ball", "avatar");
            image.setImgKey(key);
            imageRep.save(image);
            player.setAvatar(key);
            playerRep.save(player);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}
