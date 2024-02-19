package top.ulug.core.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.dto.UserDTO;

import java.util.List;

/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

@Repository
@Transactional
public interface AuthUserRepository extends JpaRepository<AuthUser, Long>,
        JpaSpecificationExecutor<AuthUser> {

    AuthUser findByUserName(String userName);

    List<AuthUser> findByUserIdIn(List<Long> ids);

    @Modifying
    @Query("update AuthUser set status='0' where id in ?1")
    void forbidden(List<Long> ids);

    @Query("select new top.ulug.core.auth.dto.UserDTO(t.userId,t.status,t.userName,t.userType,t.nickName,t.phoneNumber,t.address,t.areaCode,t.unitCode,t.orgId,t.org.orgName) from AuthUser t where t.status=:status and t.org.orgPath like :orgPath% and (t.userName like %:userName% or t.nickName like %:userName%)")
    Page<UserDTO> page(@Param("userName") String userName, @Param("orgPath") String orgPath, @Param("status") String status, Pageable pageable);

    List<AuthUser> findByUnitCodeAndStatus(String unitCode, String status);

}
