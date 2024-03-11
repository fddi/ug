package top.ulug.core.deploy.service.impl;

import org.springframework.stereotype.Service;
import top.ulug.base.dto.MessageDTO;
import top.ulug.core.deploy.service.MessageService;

/**
 * Created by liujf on 2024/3/9.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public boolean send(String name, MessageDTO dto) {
        return false;
    }
}
