package top.ulug.core.deploy.service;

import top.ulug.base.dto.AbilityDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployAbility;

import java.util.List;

/**
 * Created by liujf on 2019/3/30.
 * 逝者如斯夫 不舍昼夜
 */
public interface AbilityService {

    /**
     * 数据保存
     *
     * @param list 接口列表
     * @return result
     */
    WrapperDTO<String> saveAbility(List<AbilityDTO> list,String coreName,String appName);

    /**
     * 服务接口列表
     *
     * @param abilityNote 接口说明
     * @return 接口文档集
     */
    WrapperDTO<List<DeployAbility>> list(String abilityNote);

    /**
     * 删除接口
     *
     * @param abilityId 接口ID
     * @return bol
     */
    WrapperDTO<String> del(Long abilityId);
}
