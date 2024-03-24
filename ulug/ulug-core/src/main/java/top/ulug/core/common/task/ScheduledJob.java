package top.ulug.core.common.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.ulug.base.dto.MessageDTO;
import top.ulug.base.inf.TaskService;
import top.ulug.base.spring.SpringContextUtils;
import top.ulug.core.deploy.domain.DeployQuartzTask;
import top.ulug.core.deploy.service.SseMessageService;
import top.ulug.core.deploy.service.QuartzService;

/**
 * Created by liujf on 2021/7/23.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class ScheduledJob implements Job {
    @Autowired
    QuartzService quartzService;
    @Autowired
    SseMessageService messageService;
    @Value("${spring.application.name}")
    private String appName;
    private final String topic = "ulug-task";

    @Override
    public void execute(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        DeployQuartzTask task = quartzService.findTask(jobKey.getName());
        if (task == null || !"1".equals(task.getStatus())) {
            return;
        }
        if (appName.equals(task.getTaskGroup())) {
            try {
                this.doExecute(task.getTaskService());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            //分布式服务，发送任务执行消息
            MessageDTO<String> messageDTO = new MessageDTO<>();
            messageDTO.setTopic(topic);
            messageDTO.setReceiver(task.getTaskGroup());
            messageDTO.setMsgCode("01");
            messageDTO.setHandler(task.getTaskService());
            messageService.send(topic, messageDTO);
        }
    }

    private void doExecute(String taskService) throws Exception {
        TaskService service = (TaskService) SpringContextUtils.getBean(taskService);
        service.execute();
    }
}
