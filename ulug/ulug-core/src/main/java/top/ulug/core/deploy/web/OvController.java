package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployOv;
import top.ulug.core.deploy.service.OvService;

import java.util.List;

/**
 * Created by liujf on 2019/9/15.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/ov")
public class OvController {
    @Autowired
    private OvService ovService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "系统配置值列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<DeployOv>> list(@RequestParam(value = "optionCode") String optionCode) throws Exception {
        return ovService.listOv(optionCode);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "系统配置值保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute DeployOv ov) throws Exception {
        return ovService.save(ov);
    }

    @RequestMapping(value = "/dev/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "系统配置值删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute DeployOv ov) throws Exception {
        return ovService.del(ov);
    }

    @RequestMapping(value = "/one", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "系统配置值", paramsExample = "", resultExample = "")
    public WrapperDTO<String> getV(@RequestParam(value = "optionCode") String optionCode) throws Exception {
        return WrapperDTO.success(ovService.getOv(optionCode));
    }


    @RequestMapping(value = "/one-public", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "系统配置值", paramsExample = "", resultExample = "")
    public WrapperDTO<String> getPublicV(@RequestParam(value = "unitCode") String unitCode,
                                         @RequestParam(value = "optionCode") String optionCode) throws Exception {
        return WrapperDTO.success(ovService.getPublicOv(unitCode,optionCode));
    }
}
