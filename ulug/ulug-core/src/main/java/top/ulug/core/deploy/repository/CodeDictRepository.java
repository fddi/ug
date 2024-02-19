package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.CodeDict;

import java.util.List;

/**
 * Created by liujf on 2019/4/10.
 * 逝者如斯夫 不舍昼夜
 */

public interface CodeDictRepository extends JpaRepository<CodeDict, Long>,
        JpaSpecificationExecutor<CodeDict> {

    @Query("select dictPath from CodeDict where dictId=?1")
    String findPath(long dictId);

    CodeDict findByCatalogAndDictCode(String catalog, String dictCode);

    List<CodeDict> findByCatalogOrderByDictSort(String catalog);

    @Query(" from CodeDict t where t.catalog=?1 and t.dictPath like ?2% order by dictSort")
    List<CodeDict> findByCatalogAndDictPath(String catalog, String dictPath);

    @Query(" from CodeDict t where t.dictPath like ?1% order by dictSort")
    List<CodeDict> findByDictPath(String dictPath);

    List<CodeDict> findByCatalogAndParentId(String catalog,long parentId);
}
