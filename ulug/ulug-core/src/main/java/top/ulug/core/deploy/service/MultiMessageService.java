package top.ulug.core.deploy.service;

import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.CurdService;
import top.ulug.core.deploy.domain.MultiMessage;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:29 星期五
 */
public interface MultiMessageService extends CurdService<MultiMessage,MultiMessage> {

    /**
     * 发送消息
     * @param multiMessage 消息主体
     * @param roleIds 角色列表
     * @return 结果
     */
    public WrapperDTO<String> send(MultiMessage multiMessage,String roleIds) throws Exception;
}
