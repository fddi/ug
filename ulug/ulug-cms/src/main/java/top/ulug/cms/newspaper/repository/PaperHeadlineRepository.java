package top.ulug.cms.newspaper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.cms.newspaper.domain.PaperHeadline;

import java.util.List;

/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
public interface PaperHeadlineRepository extends JpaRepository<PaperHeadline, Long>,
        JpaSpecificationExecutor<PaperHeadline> {

    @Query("from PaperHeadline where dateItem=?1 order by type,layout")
    List<PaperHeadline> findByDateItem(String dateItem);

    PaperHeadline findByLink(String link);

    @Transactional
    @Modifying
    @Query("delete from PaperHeadline where dateItem=?1")
    void deleteByDateItem(String dateItem);
}
