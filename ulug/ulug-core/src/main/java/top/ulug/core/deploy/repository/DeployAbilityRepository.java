package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.DeployAbility;

import java.util.List;

/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface DeployAbilityRepository extends JpaRepository<DeployAbility, Long> {

    List<DeployAbility> findByProjectId(String projectId);

    List<DeployAbility> findByAbilityIdIn(List<Long> ids);

    @Query("from DeployAbility where abilityNote like %?1% or abilityUri like %?1% order by abilityId,abilityNote")
    List<DeployAbility> findByNote(String note);

    List<DeployAbility> findByAbilityUriNotLikeOrderByAbilityNote(String uri);

    @Modifying
    @Query("update DeployAbility set status=?1 where projectId=?2 ")
    Integer updateStatus(String status, String projectId);
}
