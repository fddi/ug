package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ulug.core.deploy.domain.DeployNode;


/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface DeployNodeRepository extends JpaRepository<DeployNode, Long> {

    DeployNode findByServerName(String serverName);
}
