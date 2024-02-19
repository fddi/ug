package top.ulug.cms.football.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.football.domain.BallClub;

/**
 * Created by liujf on 2020/10/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallClubRepository extends JpaRepository<BallClub, Long> {

    BallClub findByClubName(String clubName);

    BallClub findFirstByMagicCode(String magicCode);

    @Query("from BallClub t where t.countryId=?1 and t.clubName like %?2% order by countryId,clubId")
    Page<BallClub> page(Long countryId, String name, Pageable pageable);
}
