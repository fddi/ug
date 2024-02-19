package top.ulug.cms.common.stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.ulug.base.dto.MessageDTO;
import top.ulug.base.inf.TaskService;
import top.ulug.base.spring.SpringContextUtils;

import java.util.function.Consumer;

/**
 * Created by liujf on 2021/8/13.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class ConsumerTask {
    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public Consumer<MessageDTO<String>> task() {
        return (msg) -> {
            if (appName.equals(msg.getReceiver())) {
                try {
                    doExecute(msg.getHandler());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private void doExecute(String taskService) throws Exception {
        TaskService service = (TaskService) SpringContextUtils.getBean(taskService);
        service.execute();
    }
}
