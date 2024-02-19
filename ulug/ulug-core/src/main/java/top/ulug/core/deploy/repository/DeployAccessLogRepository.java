package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.DeployAccessLog;

import java.util.Date;

/**
 * Created by liujf on 2020-09-11.
 * 逝者如斯夫 不舍昼夜
 */
public interface DeployAccessLogRepository extends JpaRepository<DeployAccessLog, Long> {

    @Query("select count(*) from DeployAccessLog where gmtCreate>=?1")
    Long pvToday(Date date);
}
