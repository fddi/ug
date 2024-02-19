package top.ulug.cms.football.magic.pipeline;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.repository.BallCountryRepository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * Created by fddiljf on 2017/3/21.
 * 逝者如斯夫 不舍昼夜
 */
public class CountryCaiPagePipeline implements Pipeline {
    Logger logger = LoggerFactory.getLogger(CountryCaiPagePipeline.class);
    private BallCountryRepository countryRepository;

    public CountryCaiPagePipeline(BallCountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        logger.info("get page: {}", resultItems.getRequest().getUrl());
        String json = (String) resultItems.getAll().get("list");
        List<BallCountry> list = JSON.parseArray(json, BallCountry.class);
        if (list != null) {
            countryRepository.saveAll(list);
        }
    }
}
