package top.ulug.cms.football.service;

/**
 * Created by liujf on 2020/10/29.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallTakeService {

    /**
     * 国家
     */
    void takeCountry();

    /**
     * 国家英文名数据
     */
    void takeCountryDetail();

    /**
     * 国家旗帜
     */
    void takeCountryFlag();

    /**
     * 俱乐部
     *
     * @param countryName 所属国家
     */
    void takeClub(String countryName);

    /**
     * 运动员
     *
     * @param countryName 所属国家
     */
    void takePlayer(String countryName);

    /**
     * 获取当期比赛
     */
    void takeMatch();

    /**
     * 生成每日比赛
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void createArticle(String dateItem) throws Exception;
}
