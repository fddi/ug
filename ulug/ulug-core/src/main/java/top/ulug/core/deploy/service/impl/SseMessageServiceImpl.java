package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ulug.base.dto.MessageDTO;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.core.deploy.service.CacheService;
import top.ulug.core.deploy.service.SseMessageService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liujf on 2024/3/9.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class SseMessageServiceImpl implements SseMessageService {
    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    private CacheService cacheService;

    @Override
    public SseEmitter connect(String token) {
        SseEmitter sseEmitter = new SseEmitter();
        // 连接成功需要返回数据，否则会出现待处理状态
        try {
            sseEmitter.send(SseEmitter.event().comment("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 连接断开
        sseEmitter.onCompletion(() -> {
            sseEmitterMap.remove(token);
        });

        // 连接超时
        sseEmitter.onTimeout(() -> {
            sseEmitterMap.remove(token);
        });

        // 连接报错
        sseEmitter.onError((throwable) -> {
            sseEmitterMap.remove(token);
        });

        sseEmitterMap.put(token, sseEmitter);
        return sseEmitter;
    }

    @Override
    public boolean send(String userName, MessageDTO dto) {
        String appId = requestUtils.getCurrentAppId();
        List<String> tokens = cacheService.getTokens(appId, userName);
        if (tokens != null) {
            sseEmitterMap.forEach((token, sseEmitter) -> {
                for (int i = 0; i < tokens.size(); i++) {
                    String loginToken = tokens.get(i);
                    if (token.equals(loginToken)) {
                        try {
                            sseEmitter.send(dto, MediaType.APPLICATION_JSON);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
            });
        }
        return false;
    }
}
