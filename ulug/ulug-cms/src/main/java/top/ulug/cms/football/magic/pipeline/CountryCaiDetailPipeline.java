package top.ulug.cms.football.magic.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.repository.BallCountryRepository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by fddiljf on 2017/3/21.
 * 逝者如斯夫 不舍昼夜
 */
public class CountryCaiDetailPipeline implements Pipeline {
    Logger logger = LoggerFactory.getLogger(CountryCaiDetailPipeline.class);
    private BallCountryRepository countryRepository;

    public CountryCaiDetailPipeline(BallCountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        logger.info("get page: {}", resultItems.getRequest().getUrl());
        String name = (String) resultItems.getAll().get("name");
        String enName = (String) resultItems.getAll().get("enName");
        BallCountry country = countryRepository.findByCountryName(name);
        if (country != null) {
            country.setEnName(enName);
            countryRepository.save(country);
        }
    }
}
