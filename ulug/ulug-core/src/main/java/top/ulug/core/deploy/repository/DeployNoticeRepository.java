package top.ulug.core.deploy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.DeployNotice;

/**
 * Created by liujf on 2022/7/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface DeployNoticeRepository extends JpaRepository<DeployNotice, Long> {

    @Query("from DeployNotice")
    Page<DeployNotice> page(Pageable pageable);

    @Query(" from DeployNotice t where t.status='1' and t.level='1'")
    Page<DeployNotice> pagePublic(Pageable pageable);
}
