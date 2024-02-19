package top.ulug.core.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.auth.domain.AuthOrg;

import java.util.List;

/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface AuthOrgRepository extends JpaRepository<AuthOrg, Long>,
        JpaSpecificationExecutor<AuthOrg> {

    @Query(" from AuthOrg t where t.orgType='01' and t.unitCode=?1 ")
    AuthOrg findByUnitCode(String unitCode);

    @Query(" from AuthOrg t where t.orgCode=?1 ")
    AuthOrg findByOrgCode(String orgCode);

    @Query("select orgPath from AuthOrg where orgId=?1")
    String findPath(long orgId);

    @Query(" from AuthOrg t where t.orgPath like ?1% order by orgSort")
    List<AuthOrg> findByOrgPath(String orgPath);

    @Query(" from AuthOrg t where t.areaCode=?1 and t.orgType<>'De' order by orgSort")
    List<AuthOrg> findByAreaCode(String areaCode);

    List<AuthOrg> findByUnitCodeOrderByOrgSort(String unitCode);

    @Query("from AuthOrg t where t.unitCode=?1 and t.status='1' order by orgSort")
    List<AuthOrg> findActive(String unitCode);

    @Query(" from AuthOrg t where t.orgType='01' and t.areaCode=?1 and (t.orgName like %?2% or t.unitCode like %?2% or t.orgCode like %?1%) order by orgSort ")
    Page<AuthOrg> page(String areaCode, String orgName, Pageable pageable);

    List<AuthOrg> findByOrgIdIn(List<Long> ids);

    @Query(" from AuthOrg t where (t.orgCode like %?1% or t.orgName like %?1% or t.unitCode like %?1%) and t.orgType=?2")
    List<AuthOrg> search(String searchKey, String orgType);
}
