package top.ulug.cms.football.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.football.domain.BallMatch;
import top.ulug.cms.football.dto.MatchDTO;

import java.util.List;

/**
 * Created by liujf on 2020/10/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallMatchRepository extends JpaRepository<BallMatch, Long> {

    List<BallMatch> findByPeriod(String period);

    @Query("select new top.ulug.cms.football.dto.MatchDTO(t.matchId, t.systemId, t.homeName, t.visitingName, t.matchRound, t.matchDate, t.matchTime, t.matchCity, t.matchStadium, t.score1, t.score2, t.matchStatus, c1.flag, c2.flag) from BallMatch t join BallCountry c1 on t.homeName = c1.countryName join BallCountry c2 on t.visitingName = c2.countryName where t.systemId=?1 and (t.homeName like %?2% or t.visitingName like %?2%) order by t.matchDate desc,t.matchTime desc")
    Page<MatchDTO> pageCountry(Long systemId, String name, Pageable pageable);

    @Query("select new top.ulug.cms.football.dto.MatchDTO(t.matchId, t.systemId, t.homeName, t.visitingName, t.matchRound, t.matchDate, t.matchTime, t.matchCity, t.matchStadium, t.score1, t.score2, t.matchStatus, c1.flag, c2.flag) from BallMatch t join BallClub c1 on t.homeName = c1.clubName join BallClub c2 on t.visitingName = c2.clubName where t.systemId=?1 and (t.homeName like %?2% or t.visitingName like %?2%) order by t.matchDate desc,t.matchTime desc")
    Page<MatchDTO> pageClub(Long systemId, String name, Pageable pageable);
}
