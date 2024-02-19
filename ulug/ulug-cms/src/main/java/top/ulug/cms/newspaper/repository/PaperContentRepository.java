package top.ulug.cms.newspaper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.cms.newspaper.domain.PaperContent;

/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
public interface PaperContentRepository extends JpaRepository<PaperContent, Long>,
        JpaSpecificationExecutor<PaperContent> {

    @Transactional
    @Modifying
    @Query("delete from PaperContent where dateItem=?1")
    void deleteByDateItem(String dateItem);

    PaperContent findByLineId(Long lineId);
}
