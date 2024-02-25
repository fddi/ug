package top.ulug.core.deploy.service;

import top.ulug.base.dto.WrapperDTO;

import java.util.Map;

/**
 * Created by liujf on 2020-09-16.
 * 逝者如斯夫 不舍昼夜
 */
public interface AnalysisService {

    /**
     * 当日访问量
     *
     * @return num
     */
    WrapperDTO<Long> pvToday();

}
