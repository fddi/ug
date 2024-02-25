package top.ulug.core.deploy.service;


import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployClient;
import top.ulug.base.inf.CurdService;

import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2019/3/24.
 * 逝者如斯夫 不舍昼夜
 */
public interface ClientService extends CurdService<DeployClient, DeployClient> {

    /**
     * 客户端安全校验
     *
     * @param params 参数
     * @return bool
     */
    boolean checkClient(Map<String, String> params);

    /**
     * 返回客户端秘钥
     *
     * @param clientId id
     * @return key
     */
    WrapperDTO<String> getClientKey(Long clientId);

    /**
     * 返回客户端列表
     *
     * @return list
     */
    WrapperDTO<List<LabelDTO>> clientList();
}
