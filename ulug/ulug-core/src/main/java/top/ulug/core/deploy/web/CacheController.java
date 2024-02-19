package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.service.CacheService;

import java.util.List;

/**
 * Created by liujf on 2019/9/12.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/size", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "缓存占用情况", paramsExample = "", resultExample = "")
    public WrapperDTO<List<String>> getSize() throws Exception {
        return cacheService.getCacheSize();
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "缓存清除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> clear(@RequestParam(value = "tag", required = false) String tag) throws Exception {
        return cacheService.clear(tag);
    }
}
