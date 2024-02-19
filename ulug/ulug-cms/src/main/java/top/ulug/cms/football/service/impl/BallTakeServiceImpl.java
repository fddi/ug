package top.ulug.cms.football.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.football.domain.BallClub;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.domain.BallMatch;
import top.ulug.cms.football.domain.BallSystem;
import top.ulug.cms.football.magic.pipeline.ClubPipeline;
import top.ulug.cms.football.magic.pipeline.CountryCaiDetailPipeline;
import top.ulug.cms.football.magic.pipeline.CountryCaiPagePipeline;
import top.ulug.cms.football.magic.pipeline.PlayerPipeline;
import top.ulug.cms.football.magic.processor.*;
import top.ulug.cms.football.repository.*;
import top.ulug.cms.football.service.BallTakeService;
import top.ulug.cms.media.domain.MediaContent;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.repository.MediaContentRepository;
import top.ulug.cms.media.repository.MediaImageRepository;
import top.ulug.cms.media.repository.MediaSubjectRepository;
import top.ulug.core.api.service.OvService;
import ug.template.engine.core.launcher.CombExecutor;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liujf on 2020/10/29.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class BallTakeServiceImpl implements BallTakeService {
    Logger logger = LoggerFactory.getLogger(BallTakeServiceImpl.class);
    @Autowired
    BallCountryRepository countryRepository;
    @Autowired
    MediaImageRepository imageRepository;
    @Autowired
    BallClubRepository clubRepository;
    @Autowired
    BallPlayerRepository playerRepository;
    @Autowired
    BallSystemRepository systemRepository;
    @Autowired
    BallMatchRepository matchRepository;
    @Autowired
    MediaSubjectRepository subjectRepository;
    @Autowired
    MediaContentRepository contentRepository;
    @Autowired
    OvService ovService;
    @Autowired
    CombExecutor combExecutor;

    @Override
    public void takeCountry() {
        Spider.create(new CountryCaiPageProcessor())
                .addPipeline(new CountryCaiPagePipeline(countryRepository))
                .addUrl("http://liansai.500.com/paiming/")
                .thread(1).start();
    }

    @Override
    public void takeCountryDetail() {
        Spider.create(new CountryCaiDetailProcessor())
                .addPipeline(new CountryCaiDetailPipeline(countryRepository))
                .addUrl("http://liansai.500.com/paiming/")
                .thread(1).run();
    }

    @Override
    public void takeCountryFlag() {
        Spider.create(new CountryFIFAPageProcessor(countryRepository, imageRepository))
                .addPipeline(new ConsolePipeline())
                .addUrl("http://www.fifa.com/fifa-world-ranking/ranking-table/men/index.html")
                .thread(1).run();
    }

    @Override
    public void takeClub(String countryName) {
        String url = getCaiUrl(countryName);
        if (url == null) {
            logger.error("params is null");
            return;
        }
        BallCountry country = null;
        if (!"欧冠".equals(countryName) && !"欧罗巴".equals(countryName)) {
            country = countryRepository.findByCountryName(countryName);
            if (country == null) {
                logger.error("params is null");
                return;
            }
        }
        Spider.create(new ClubPageProcessor())
                .addPipeline(new ClubPipeline(country, imageRepository, clubRepository))
                .addUrl(url)
                .thread(1).start();
    }

    @Override
    public void takePlayer(String countryName) {
        String url = getCaiUrl(countryName);
        if (url == null) {
            logger.error("params is null");
            return;
        }
        Spider.create(new PlayerPageProcessor(countryName, url, playerRepository))
                .addPipeline(new PlayerPipeline(imageRepository, countryRepository,
                        clubRepository, playerRepository))
                .addUrl(url)
                .thread(1).start();
    }

    @Override
    public void takeMatch() {
        Spider.create(new MatchProcessor(systemRepository, matchRepository))
                .addPipeline(new ConsolePipeline())
                .addUrl("https://live.500.com/2h1.php")
                .thread(1).run();
    }

    @Override
    public void createArticle(String dateItem) throws Exception {
        if (StringUtils.isEmpty(dateItem)) {
            logger.error("足球推送文章自动生成错误：参数 {} 为空", dateItem);
            return;
        }
        Date date = DateTimeUtils.getDate(dateItem, "yyyyMMdd");
        dateItem = DateTimeUtils.getDateTime(date, "yyyy-MM-dd");
        String dateDesc = DateTimeUtils.getDateTime(date, "yyyy年MM月dd日");
        List<BallMatch> list = matchRepository.findByPeriod(dateItem);
        if (list == null || list.size() == 0) {
            return;
        }
        String summary = dateDesc + "足球比賽推送,世界杯_欧洲杯_美洲杯_中超_西甲_英超_意甲_德甲_法甲联赛_欧冠_欧罗巴赛程";
        StringBuilder htmlTable = new StringBuilder();
        HashMap<String, Object> params;
        htmlTable.append(combExecutor.getComb("ball.tableHeader", null));
        for (int i = 0; i < list.size(); i++) {
            BallMatch match = list.get(i);
            params = new HashMap<>();
            params.put("homeName", match.getHomeName());
            params.put("homeImg", getImgUrl(match.getHomeName()));
            params.put("visitingName", match.getVisitingName());
            params.put("visitingImg", getImgUrl(match.getVisitingName()));
            params.put("systemName", match.getSystem().getSystemName());
            params.put("matchRound", match.getMatchRound());
            params.put("matchDate", match.getMatchDate());
            htmlTable.append(combExecutor.getComb("ball.tableRow", params));
        }
        htmlTable.append("</tbody></table>");
        List<BallSystem> systemList = systemRepository.findAllCurrent(new Date());
        StringBuilder htmlLiveSource = new StringBuilder("<p><strong>官方直播地址：</strong></p>");
        htmlLiveSource.append("<ul>");
        for (int i = 0; i < systemList.size(); i++) {
            BallSystem system = systemList.get(i);
            params = new HashMap<>();
            params.put("systemImg", getImgUrl(system.getLogo()));
            params.put("systemName", system.getSystemName());
            params.put("liveSource", system.getLiveSource());
            htmlLiveSource.append(combExecutor.getComb("ball.liveSource", params));
        }
        htmlLiveSource.append("</ul>");
        MediaContent content = new MediaContent();
        MediaSubject subject = subjectRepository.findBySubjectCode("ugszqss");
        String contentTitle = String.format("%s足球赛程表", dateDesc);
        content.setSubject(subject);
        content.setContentTitle(contentTitle);
        content.setSigner(subject.getSubjectName());
        content.setSummary(summary);
        content.setArticle(htmlTable.append(htmlLiveSource).toString());
        content.setTags("足球,中超,西甲,英超,意甲,德甲,法甲,欧冠,欧罗巴,世界杯,欧洲杯,美洲杯");
        content.setPublishStatus("0");
        content.setUnitCode(subject.getUnitCode());
        content.setAreaCode(subject.getAreaCode());
        content.setSort(1);
        contentRepository.save(content);
        logger.info("足球推送文章自动生成：{}", contentTitle);
    }

    private String getCaiUrl(String countryName) {
        String url = null;
        if ("中国".equals(countryName))
            url = "https://liansai.500.com/zuqiu-5114/jifen-13894/";
        if ("英格兰".equals(countryName))
            url = "https://liansai.500.com/zuqiu-6044/jifen-16907/";
        if ("西班牙".equals(countryName))
            url = "https://liansai.500.com/zuqiu-6066/jifen-16939/";
        if ("意大利".equals(countryName))
            url = "https://liansai.500.com/zuqiu-6083/jifen-16967/";
        if ("德国".equals(countryName))
            url = "https://liansai.500.com/zuqiu-6020/jifen-16855/";
        if ("法国".equals(countryName))
            url = "https://liansai.500.com/zuqiu-5985/jifen-16764/";
        if ("欧冠".equals(countryName))
            url = "https://liansai.500.com/zuqiu-6024/teams/";
        if ("欧罗巴".equals(countryName))
            url = "https://liansai.500.com/zuqiu-6025/teams/";
        return url;
    }

    private String getImgUrl(String name) {
        String url = ovService.getPublicOv("Y", "sys_source_url").getResultData();
        url = url + "/ug/file/img/%s";
        String key = "e2ee9f108d50c7d3815e32b396e52866fc01dffe";
        BallClub homeClub = clubRepository.findByClubName(name);
        if (homeClub != null) {
            key = homeClub.getFlag();
        } else {
            BallCountry country = countryRepository.findByCountryName(name);
            if (country != null) {
                key = country.getFlag();
            }
        }
        return String.format(url, key);
    }
}
