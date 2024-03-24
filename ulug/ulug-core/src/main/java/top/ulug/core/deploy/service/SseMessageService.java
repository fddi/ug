package top.ulug.core.deploy.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ulug.base.dto.MessageDTO;

/**
 * Created by liujf on 2024/3/9.
 * 逝者如斯夫 不舍昼夜
 */
public interface SseMessageService {

    /**
     * 建立连接
     *
     * @param token 令牌
     * @return sse
     */
    SseEmitter connect(String token);

    /**
     * 发送消息
     *
     * @param userName 用户
     * @param dto      内容
     * @return bol
     */
    boolean send(String userName, MessageDTO dto);
}
