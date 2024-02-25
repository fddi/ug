package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.dto.UserDTO;
import top.ulug.core.auth.service.AccountService;
import top.ulug.base.dto.PageDTO;

/**
 * Created by liujf on 2019/9/9.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "用户列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<UserDTO>> userList(@RequestParam(value = "pageNo") Integer pageNo,
                                                 @RequestParam(value = "pageSize") Integer pageSize,
                                                 @ModelAttribute AuthUser user) throws Exception {
        return accountService.findPage(pageSize, pageNo, user);
    }

    @RequestMapping(value = "/one", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "用户详细", paramsExample = "", resultExample = "")
    public WrapperDTO<UserDTO> user(@RequestParam(value = "userId") Long userId) throws Exception {
        return accountService.findOne(userId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "用户保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> userSave(@ModelAttribute AuthUser user) throws Exception {
        return accountService.save(user);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "用户删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> userDel(@ModelAttribute AuthUser user) throws Exception {
        return accountService.del(user);
    }


    @RequestMapping(value = "/dev/file/upload", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "用户数据导入", paramsExample = "", resultExample = "")
    public WrapperDTO<String> impDict(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "dataType", required = false) String dataType) throws Exception {
        return accountService.impByExcel(file, dataType);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "用户密码重置", paramsExample = "", resultExample = "")
    public WrapperDTO<String> userReset(@RequestParam(value = "userId") Long userId) throws Exception {
        return accountService.resetPassword(userId);
    }
}
