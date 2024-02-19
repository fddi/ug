package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.service.AnalysisService;

import java.util.Map;

/**
 * Created by liujf on 2020-09-16.
 * 逝者如斯夫 不舍昼夜
 */

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    @RequestMapping(value = "/pv", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "运行统计-当日访问量", paramsExample = "", resultExample = "")
    public WrapperDTO<Long> pv() throws Exception {
        return analysisService.pvToday();
    }

    @RequestMapping(value = "/node", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "运行统计-节点数", paramsExample = "", resultExample = "")
    public WrapperDTO<Map> node() throws Exception {
        return analysisService.node();
    }
}
