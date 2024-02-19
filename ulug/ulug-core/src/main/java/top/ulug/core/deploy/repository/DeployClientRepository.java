package top.ulug.core.deploy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.DeployClient;

import java.util.List;


/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface DeployClientRepository extends JpaRepository<DeployClient, Long> {

    List<DeployClient> findByStatus(String status);

    DeployClient findByClientNameAndStatus(String clientName, String status);

    @Query("from DeployClient t where t.clientName like %?1% or clientNote like %?1% ")
    Page<DeployClient> page(String clientName, Pageable page);
}
