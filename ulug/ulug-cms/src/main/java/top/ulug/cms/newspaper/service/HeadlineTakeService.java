package top.ulug.cms.newspaper.service;

/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
public interface HeadlineTakeService {

    /**
     * 爬取所有paper
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void runAll(String dateItem);

    /**
     * 爬取人民日报
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void runPeopleData(String dateItem);

    /**
     * 爬取21世纪经济报道
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void run21Data(String dateItem);

    /**
     * 爬取证券日报
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void runSdData(String dateItem);

    /**
     * 爬取经济参考报
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void runCkData(String dateItem);

    /**
     * 生成每日读报文章
     *
     * @param dateItem 日期格式yyyyMMdd
     */
    void createArticle(String dateItem) throws Exception;
}
