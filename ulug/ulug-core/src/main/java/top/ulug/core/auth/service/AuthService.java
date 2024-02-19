package top.ulug.core.auth.service;

import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployAbility;

import java.util.List;

/**
 * Created by liujf on 2019/3/31.
 * 逝者如斯夫 不舍昼夜
 */
public interface AuthService {

    /**
     * 校验令牌
     *
     * @return bol
     */
    boolean checkToken();

    /**
     * 接口能力权限校验
     *
     * @param uri 接口地址
     * @return bol
     */
    boolean authAbility(String uri);


    /**
     * 判断是否开发账号
     *
     * @return bol
     */
    boolean checkDevOps();


    /**
     * 返回用户拥有的接口权限
     *
     * @return list
     */
    List<DeployAbility> listUserAbility();

    /**
     * 是否所属区域
     *
     * @param areaCode 区域编码
     * @return bol
     */
    boolean checkAreaCode(String areaCode);

    /**
     * 是否所属机构
     *
     * @param unitCode 机构编码
     * @return bol
     */
    boolean checkUnitCode(String unitCode);


    /**
     * 返回当前用户信息
     *
     * @return account
     */
    WrapperDTO<AccountDTO> checkAccount();

    /**
     * 刷新登陆时间戳
     */
    void refreshLoginTime();


}
