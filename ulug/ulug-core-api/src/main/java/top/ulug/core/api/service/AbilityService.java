package top.ulug.core.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.ulug.base.dto.AbilityDTO;
import top.ulug.base.dto.WrapperDTO;

import java.util.List;

/**
 * Created by liujf on 2021/10/19.
 * 逝者如斯夫 不舍昼夜
 */
@FeignClient("ulug-core")
public interface AbilityService {

    @RequestMapping("/ability/scanning-save")
    public WrapperDTO<String> saveAbility(@RequestBody List<AbilityDTO> list,
                                          @RequestParam(value = "coreName") String coreName,
                                          @RequestParam(value = "appName") String appName);

}
