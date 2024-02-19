package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.AbilityDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployAbility;
import top.ulug.core.deploy.service.AbilityService;

import java.util.List;

/**
 * Created by liujf on 2019/9/22.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("ability")
public class AbilityController {
    @Autowired
    private AbilityService abilityService;

    @RequestMapping("/dev/list")
    @ResponseBody
    @ApiDocument(note = "接口列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<DeployAbility>> list(@RequestParam(value = "abilityNote", required = false) String abilityNote) throws Exception {
        return abilityService.list(abilityNote);
    }

    @RequestMapping("/scanning-save")
    @ResponseBody
    @ApiDocument(note = "接口保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@RequestBody List<AbilityDTO> list,
                                   @RequestParam(value = "coreName") String coreName,
                                   @RequestParam(value = "appName") String appName) throws Exception {
        return abilityService.saveAbility(list, coreName, appName);
    }

    @RequestMapping("/dev/del")
    @ResponseBody
    @ApiDocument(note = "接口删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@RequestParam(value = "abilityId") Long abilityId) throws Exception {
        return abilityService.del(abilityId);
    }
}
