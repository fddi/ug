package top.ulug.core.deploy.service;

import top.ulug.base.dto.MessageDTO;

/**
 * Created by liujf on 2024/3/9.
 * 逝者如斯夫 不舍昼夜
 */
public interface MessageService {

    /**
     * 发送消息
     *
     * @param name 主题
     * @param dto  内容
     * @return bol
     */
    boolean send(String name, MessageDTO dto);
}
