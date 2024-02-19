package top.ulug.cms.football.magic.processor;

import top.ulug.base.util.DateTimeUtils;
import top.ulug.cms.football.domain.BallMatch;
import top.ulug.cms.football.domain.BallSystem;
import top.ulug.cms.football.repository.BallMatchRepository;
import top.ulug.cms.football.repository.BallSystemRepository;
import us.codecraft.webmagic.Page;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by fddiljf on 2017/3/23.
 * 逝者如斯夫 不舍昼夜
 */
public class MatchProcessor extends BaseProcessor {
    private BallSystemRepository systemRepository;
    private BallMatchRepository matchRepository;

    public MatchProcessor(BallSystemRepository systemRepository, BallMatchRepository matchRepository) {
        this.systemRepository = systemRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public void process(Page page) {
        List<String> infoList = page.getHtml().xpath("//table[@id='table_match']/tbody/tr/@gy").all();
        List<String> roundList = page.getHtml().xpath("//table[@id='table_match']/tbody/tr/td[3]/text()").all();
        List<String> timeList = page.getHtml().xpath("//table[@id='table_match']/tbody/tr/td[4]/text()").all();
        String dateItem = DateTimeUtils.getDateItem();
        if (infoList == null || roundList == null || timeList == null) {
            logger.error("{}期:比赛列表获取失败！", dateItem);
            return;
        }
        List<BallMatch> list = new ArrayList<>();
        List<Map<String, Object>> sysList = systemRepository.findCurrent(new Date());
        Date nowDate = new Date();
        Calendar cr = Calendar.getInstance();
        cr.add(Calendar.DAY_OF_YEAR, 1);
        Date lastDate = cr.getTime();
        for (int i = 0; i < infoList.size(); i++) {
            String matchDateItem = null;
            try {
                matchDateItem = URLEncoder.encode(timeList.get(i), "utf-8")
                        .replaceAll("%C2%A0", " ");
                matchDateItem = URLDecoder.decode(matchDateItem, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Calendar cr1 = Calendar.getInstance();
            Calendar cr2 = Calendar.getInstance();
            cr1.setTime(DateTimeUtils.getDate(matchDateItem, "MM-dd HH:mm"));
            cr1.set(Calendar.YEAR, cr2.get(Calendar.YEAR));
            Date matchDate = cr1.getTime();
            if (matchDate.getTime() < nowDate.getTime() ||
                    matchDate.getTime() > lastDate.getTime()) {
                continue;
            }
            BallSystem system = null;
            String[] info = infoList.get(i).split(",");
            for (int j = 0; j < sysList.size(); j++) {
                Map<String, Object> map = sysList.get(j);
                if (info[0].equals(String.valueOf(map.get("systemName")))) {
                    system = systemRepository.findBySystemName(info[0]);
                    break;
                }
            }
            if (system == null) {
                continue;
            }
            BallMatch match = new BallMatch();
            match.setSystem(system);
            match.setMatchRound(roundList.get(i));
            match.setMatchDate(matchDateItem);
            match.setHomeName(info[1]);
            match.setVisitingName(info[2]);
            match.setPeriod(dateItem);
            list.add(match);
        }
        matchRepository.saveAll(list);
        logger.info("{}期:比赛列表获取完成，共有{}比賽", dateItem, list.size());
    }

}
