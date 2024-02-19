package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.service.AccountService;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.deploy.service.ClientService;

import java.util.Map;

/**
 * Created by liujf on 2021/6/11.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    ClientService clientService;
    @Autowired
    AuthService authService;
    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "系统登录", paramsExample = "", resultExample = "")
    public WrapperDTO<AccountDTO> login(@RequestParam(value = "userName") String userName,
                                        @RequestParam(value = "password") String password) throws Exception {
        return accountService.login(userName, password);
    }

    @RequestMapping(value = "/pwd-modify", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "密码修改", paramsExample = "", resultExample = "")
    public WrapperDTO<String> modifyPwd(@RequestParam(value = "userName") String userName,
                                        @RequestParam(value = "password") String password,
                                        @RequestParam(value = "newPassword") String newPassword,
                                        @RequestParam(value = "newPassword2") String newPassword2) throws Exception {
        return accountService.changePassword(userName, password, newPassword, newPassword2);
    }

    @PostMapping("/check")
    @ApiDocument(note = "校验权限", paramsExample = "", resultExample = "")
    boolean checkToken(@RequestParam(value = "uri") String uri) {
        boolean check = authService.checkToken();
        if (!check) return false;
        return authService.authAbility(uri);
    }

    @PostMapping("/check-client")
    @ApiDocument(note = "校验客户端", paramsExample = "", resultExample = "")
    boolean checkClient(@RequestBody Map<String, String> params) {
        return clientService.checkClient(params);
    }

    @PostMapping("/check-dev")
    @ApiDocument(note = "校验开发者权限", paramsExample = "", resultExample = "")
    boolean checkDevOps() {
        return authService.checkDevOps();
    }

    @PostMapping("/check-area")
    @ApiDocument(note = "校验地区", paramsExample = "", resultExample = "")
    boolean checkAreaCode(@RequestParam(value = "areaCode") String areaCode) {
        return authService.checkAreaCode(areaCode);
    }

    @PostMapping("/check-unit")
    @ApiDocument(note = "校验单位", paramsExample = "", resultExample = "")
    boolean checkUnitCode(@RequestParam(value = "unitCode") String unitCode) {
        return authService.checkUnitCode(unitCode);
    }

    @PostMapping("/account-info")
    @ApiDocument(note = "当前用户", paramsExample = "", resultExample = "")
    WrapperDTO<AccountDTO> checkAccountInfo() {
        return authService.checkAccount();
    }
}
