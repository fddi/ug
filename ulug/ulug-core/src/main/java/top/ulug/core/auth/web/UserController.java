package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.dto.UserDTO;
import top.ulug.core.auth.service.AccountService;
import top.ulug.base.dto.PageDTO;
import top.ulug.core.auth.service.AvatarService;

/**
 * Created by liujf on 2019/9/9.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AvatarService avatarService;

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

    @RequestMapping(value = "/file/upload-avatar", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "头像上传", paramsExample = "", resultExample = "")
    public WrapperDTO<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {
        try {
            return avatarService.upload(avatar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/file/avatar/{userName}", method = RequestMethod.GET)
    @ApiDocument(note = "头像", paramsExample = "", resultExample = "")
    public ResponseEntity<byte[]> readAvatar(
            @PathVariable String userName) throws Exception {
        return avatarService.read(userName);
    }

    @RequestMapping(value = "/update-nickname", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "更新昵称", paramsExample = "", resultExample = "")
    public WrapperDTO<String> updateNickname(@RequestParam(value = "nickname") String nickname) throws Exception {
        return accountService.updateNickname(nickname);
    }

    @RequestMapping(value = "/update-context", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "更新联系方式", paramsExample = "", resultExample = "")
    public WrapperDTO<String> updateContext(@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                            @RequestParam(value = "address", required = false) String address) throws Exception {
        return accountService.updateContext(phoneNumber, address);
    }
}
