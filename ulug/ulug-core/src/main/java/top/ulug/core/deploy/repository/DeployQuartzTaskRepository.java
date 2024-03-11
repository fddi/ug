package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ulug.core.deploy.domain.DeployQuartzTask;

import java.util.List;

/**
 * Created by liujf on 2020-10-28.
 * 逝者如斯夫 不舍昼夜
 */
public interface DeployQuartzTaskRepository extends JpaRepository<DeployQuartzTask, Long> {

    DeployQuartzTask findByTaskName(String taskName);

    List<DeployQuartzTask> findByTaskGroup(String taskGroup);

    List<DeployQuartzTask> findByTaskNameLikeOrderByTaskName(String taskName);
}
