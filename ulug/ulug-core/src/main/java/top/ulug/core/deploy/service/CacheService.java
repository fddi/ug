package top.ulug.core.deploy.service;

import top.ulug.base.dto.WrapperDTO;

import java.util.List;

/**
 * Created by liujf on 2019/9/12.
 * 逝者如斯夫 不舍昼夜
 */
public interface CacheService {

    /**
     * 获取缓存占用内存大小
     *
     * @return size
     */
    WrapperDTO<List<String>> getCacheSize();

    /**
     * 清除缓存,tag为空则清空缓存
     *
     * @param tag 缓存tag
     * @return result
     */
    WrapperDTO<String> clear(String tag);
}
