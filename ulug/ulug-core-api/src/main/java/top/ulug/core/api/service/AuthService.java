package top.ulug.core.api.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;

import java.util.Map;

/**
 * Created by liujf on 2021/8/17.
 * 逝者如斯夫 不舍昼夜
 */
@FeignClient("ulug-core")
public interface AuthService {

    @PostMapping("/auth/check")
    boolean checkToken(@RequestParam(value = "uri") String uri);

    @PostMapping("/auth/check-client")
    boolean checkClient(@RequestBody Map<String, String> params);

    @PostMapping("/auth/check-dev")
    boolean checkDevOps();

    @PostMapping("/auth/check-area")
    boolean checkArea(@RequestParam(value = "areaCode") String areaCode);

    @PostMapping("/auth/check-unit")
    boolean checkUnit(@RequestParam(value = "unitCode") String unitCode);

    @PostMapping("/auth/account-info")
    WrapperDTO<AccountDTO> checkAccount();
}
