package top.ulug.core.deploy.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.e.SQLikeEnum;
import top.ulug.base.util.StringUtils;
import top.ulug.core.common.task.ScheduledJob;
import top.ulug.core.deploy.domain.DeployQuartzTask;
import top.ulug.core.deploy.repository.DeployQuartzTaskRepository;
import top.ulug.core.deploy.service.QuartzService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020-10-28.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    private DeployQuartzTaskRepository taskRepository;
    @Autowired
    private Scheduler scheduler;

    @Override
    public DeployQuartzTask findTask(String taskName) {
        return taskRepository.findByTaskName(taskName);
    }

    @Override
    public WrapperDTO<List<DeployQuartzTask>> list(String taskName) throws Exception {
        taskName = taskName == null ? "" : taskName;
        taskName = StringUtils.linkSQLike(taskName, SQLikeEnum.ALL);
        List<DeployQuartzTask> list = taskRepository
                .findByTaskNameLikeOrderByTaskName(taskName);
        for (DeployQuartzTask task : list) {
            CronExpression cronExpression = new CronExpression(task.getCronExp());
            task.setNextDate(new Timestamp(cronExpression.getNextValidTimeAfter(new Date()).getTime()));
        }
        return WrapperDTO.success(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> build(DeployQuartzTask task) throws Exception {
        DeployQuartzTask localTask = taskRepository.save(task);
        if (task.getTaskId() == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "task");
        }
        localTask.setCronExp(task.getCronExp());
        TriggerKey triggerKey = TriggerKey.triggerKey(localTask.getTaskName(), localTask.getTaskGroup());
        Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
        if (state != Trigger.TriggerState.NONE) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
        }
        JobDetail job = JobBuilder.newJob(ScheduledJob.class)
                .withIdentity(localTask.getTaskName(), localTask.getTaskGroup()).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(localTask.getCronExp());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(localTask.getTaskName(), localTask.getTaskGroup())
                .startNow().withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(job, trigger);
        localTask.setStatus("1");
        taskRepository.save(localTask);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> remove(DeployQuartzTask task) throws Exception {
        Optional<DeployQuartzTask> optional = taskRepository.findById(task.getTaskId());
        if (!optional.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "task");
        }
        task = optional.get();
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), task.getTaskGroup());
        Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
        if (state != Trigger.TriggerState.NONE) {
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
        }
        scheduler.deleteJob(JobKey.jobKey(task.getTaskName(), task.getTaskGroup()));
        taskRepository.delete(task);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> jobControl(Long taskId, Integer tag) throws Exception {
        Optional<DeployQuartzTask> optional = taskRepository.findById(taskId);
        if (optional.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "task");
        }
        DeployQuartzTask task = optional.get();
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName(), task.getTaskGroup());
        Trigger.TriggerState state = scheduler.getTriggerState(triggerKey);
        if (state == Trigger.TriggerState.NONE) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "TriggerState");
        }
        String status = task.getStatus();
        switch (tag) {
            case 1:
                scheduler.triggerJob(JobKey.jobKey(task.getTaskName(), task.getTaskGroup()));
                break;
            case 2:
                scheduler.pauseJob(JobKey.jobKey(task.getTaskName(), task.getTaskGroup()));
                status = "-1";
                break;
            case 3:
                scheduler.resumeJob(JobKey.jobKey(task.getTaskName(), task.getTaskGroup()));
                status = "1";
                break;
            default:
                break;

        }
        task.setStatus(status);
        taskRepository.save(task);
        return WrapperDTO.success();
    }

}
