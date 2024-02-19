package top.ulug.core.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.ulug.base.dto.WrapperDTO;

/**
 * Created by liujf on 2021/8/17.
 * 逝者如斯夫 不舍昼夜
 */
@FeignClient("ulug-core")
public interface OvService {

    @PostMapping(value = "/ov/one")
    public WrapperDTO<String> getOv(@RequestParam(value = "optionCode") String optionCode);


    @PostMapping(value = "/ov/one-public")
    public WrapperDTO<String> getPublicOv(@RequestParam(value = "unitCode") String unitCode,
                                          @RequestParam(value = "optionCode") String optionCode);

}
