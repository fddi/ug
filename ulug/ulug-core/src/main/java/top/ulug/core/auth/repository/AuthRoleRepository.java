package top.ulug.core.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.dto.RoleDTO;

import java.util.List;

/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface AuthRoleRepository extends JpaRepository<AuthRole, Long>,
        JpaSpecificationExecutor<AuthRole> {

    List<AuthRole> findAllByUserList_UserName(String userName);

    @Query(" select new top.ulug.core.auth.dto.RoleDTO(t.roleId,t.roleName,t.roleNote,t.roleType,t.orgId,t.org.orgName) from AuthRole t where t.org.orgPath like ?1% and t.roleName like %?2%")
    Page<RoleDTO> page(String orgPath, String roleName, Pageable pageable);

    List<AuthRole> findByRoleIdIn(List<Long> ids);
}
