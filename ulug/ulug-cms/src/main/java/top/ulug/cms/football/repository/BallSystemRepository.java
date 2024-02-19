package top.ulug.cms.football.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.base.dto.TreeDTO;
import top.ulug.cms.football.domain.BallSystem;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2020/10/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallSystemRepository extends JpaRepository<BallSystem, Long> {

    @Query("from BallSystem t where t.systemName like %?1% or enName like %?1% order by endDate desc")
    Page<BallSystem> page(String systemName, Pageable page);

    @Query("select new map(t.systemName as systemName,max(t.gmtCreate) as gmtCreate) from BallSystem t where t.endDate is null or t.endDate>?1 group by t.systemName")
    List<Map<String, Object>> findCurrent(Date now);

    @Query("from BallSystem t where t.endDate is null or t.endDate>?1  group by t.systemName ")
    List<BallSystem> findAllCurrent(Date now);

    BallSystem findBySystemName(String systemName);

    @Query("select new top.ulug.base.dto.TreeDTO(systemId,0L,concat(systemId,''),concat(systemId,''),concat(season,systemName),concat(season,systemName)) from BallSystem order by endDate desc")
    List<TreeDTO> findTree();
}
