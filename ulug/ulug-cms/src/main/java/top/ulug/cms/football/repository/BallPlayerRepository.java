package top.ulug.cms.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.football.domain.BallPlayer;

/**
 * Created by liujf on 2020/10/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallPlayerRepository extends JpaRepository<BallPlayer, Long> {

    @Query("select count(*) as num from BallPlayer t where t.club.clubName=?1")
    Long findCountByClubName(String clubName);
}
