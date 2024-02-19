package top.ulug.core.deploy.service;

import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployQuartzTask;

import java.util.List;

/**
 * Created by liujf on 2020-10-28.
 * 逝者如斯夫 不舍昼夜
 */
public interface QuartzService {

    /**
     * 获取任务
     *
     * @param taskName 任务名称(ID)
     * @return 任务信息
     */
    DeployQuartzTask findTask(String taskName);

    /**
     * 任务列表
     *
     * @param taskName 名称
     * @return list
     */
    WrapperDTO<List<DeployQuartzTask>> list(String taskName) throws Exception;

    /**
     * 创建调度任务
     *
     * @param task 任务
     * @return result dto
     * @throws Exception
     */
    WrapperDTO<String> build(DeployQuartzTask task) throws Exception;

    /**
     * 删除任务
     *
     * @param task 任务
     * @return result dto
     */
    WrapperDTO<String> remove(DeployQuartzTask task) throws Exception;

    /**
     * 立即运行任务
     *
     * @param taskId 运行任务
     * @return result dto
     */
    WrapperDTO<String> jobControl(Long taskId, Integer tag) throws Exception;
}
