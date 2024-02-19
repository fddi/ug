package top.ulug.cms.football.magic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by fddiljf on 2017/3/21.
 * 逝者如斯夫 不舍昼夜
 */
public class BaseProcessor implements PageProcessor {

    protected static Logger logger = LoggerFactory.getLogger(BaseProcessor.class);

    private final Site site = Site.me().setRetryTimes(5).setSleepTime(5000).setTimeOut(30000).setCharset("gb2312")
            .addHeader("Accept-Encoding", "/").setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }
}
