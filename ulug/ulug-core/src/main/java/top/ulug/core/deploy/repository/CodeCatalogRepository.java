package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.CodeCatalog;

import java.util.List;

/**
 * Created by liujf on 2019/4/10.
 * 逝者如斯夫 不舍昼夜
 */

public interface CodeCatalogRepository extends JpaRepository<CodeCatalog, Long>,
        JpaSpecificationExecutor<CodeCatalog> {

    CodeCatalog findByCatalogCode(String catalogCode);

    @Query("select catalogPath from CodeCatalog where catalogId=?1")
    String findPath(long catalogId);

    @Query(" from CodeCatalog t where t.catalogPath like ?1% order by catalogSort")
    List<CodeCatalog> findByCatalogPath(String catalogPath);
}
