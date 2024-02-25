package top.ulug.core.deploy.service;


import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployOv;
import top.ulug.base.inf.CurdService;

import java.util.List;

/**
 * Created by liujf on 2019/3/24.
 * 逝者如斯夫 不舍昼夜
 */
public interface OvService extends CurdService<DeployOv, DeployOv> {

    /**
     * 获取配置项值
     *
     * @param optionCode 配置项代码
     * @return v
     */
    String getOv(String optionCode);

    /**
     * 获取公开配置项值
     *
     * @param unitCode   单位
     * @param optionCode 配置项代码
     * @return v
     */
    String getPublicOv(String unitCode, String optionCode);

    /**
     * @param optionCode 配置项代码
     * @param value      v
     * @return bol
     */
    boolean saveOv(String optionCode, String value);

    /**
     * 获取配置项值列表
     *
     * @param optionCode 配置项代码
     * @return 值列表
     */
    WrapperDTO<List<DeployOv>> listOv(String optionCode);
}
