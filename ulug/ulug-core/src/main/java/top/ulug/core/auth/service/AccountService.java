package top.ulug.core.auth.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.dto.UserDTO;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2019/3/25.
 * 逝者如斯夫 不舍昼夜
 */
public interface AccountService extends CurdService<AuthUser, UserDTO> {

    /**
     * 登录
     *
     * @param loginId  用户名
     * @param password 密码
     * @return result
     */
    WrapperDTO<AccountDTO> login(String loginId, String password);

    /**
     * 退出登录
     *
     * @return bol
     */
    boolean logout();

    /**
     * 重置密码
     *
     * @param userId 用户id
     * @return result
     */
    WrapperDTO<String> resetPassword(Long userId);

    /**
     * 修改密码
     *
     * @param userName 用户名
     * @param pwd      密码
     * @param npwd     新密码
     * @param npwd2    新密码2次
     * @return 修改结果
     */
    WrapperDTO<String> changePassword(String userName, String pwd, String npwd, String npwd2) throws Exception;


    /**
     * 导入数据
     *
     * @param file     EXCEL
     * @param dataType 数据类型
     * @return result
     * @throws Exception exp
     */
    WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception;

}
