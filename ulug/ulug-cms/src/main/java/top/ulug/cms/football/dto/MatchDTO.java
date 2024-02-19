package top.ulug.cms.football.dto;

import java.io.Serializable;

/**
 * @Author liu
 * @Date 2022/9/6 21:48 星期二
 */
public class MatchDTO implements Serializable {
    public MatchDTO() {
    }

    public MatchDTO(Long matchId, Long systemId, String homeName, String visitingName, String matchRound, String matchDate, String matchTime, String matchCity, String matchStadium, String score1, String score2, String matchStatus, String homeFLag, String visitingFlag) {
        this.matchId = matchId;
        this.systemId = systemId;
        this.homeName = homeName;
        this.visitingName = visitingName;
        this.matchRound = matchRound;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.matchCity = matchCity;
        this.matchStadium = matchStadium;
        this.score1 = score1;
        this.score2 = score2;
        this.matchStatus = matchStatus;
        this.homeFLag = homeFLag;
        this.visitingFlag = visitingFlag;
    }

    private Long matchId;
    private Long systemId;
    private String homeName;
    private String visitingName;
    private String matchRound;
    private String matchDate;
    private String matchTime;
    private String matchCity;
    private String matchStadium;
    private String score1;
    private String score2;
    private String matchStatus;
    private String period;
    private String homeFLag;
    private String visitingFlag;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getVisitingName() {
        return visitingName;
    }

    public void setVisitingName(String visitingName) {
        this.visitingName = visitingName;
    }

    public String getMatchRound() {
        return matchRound;
    }

    public void setMatchRound(String matchRound) {
        this.matchRound = matchRound;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchCity() {
        return matchCity;
    }

    public void setMatchCity(String matchCity) {
        this.matchCity = matchCity;
    }

    public String getMatchStadium() {
        return matchStadium;
    }

    public void setMatchStadium(String matchStadium) {
        this.matchStadium = matchStadium;
    }

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getHomeFLag() {
        return homeFLag;
    }

    public void setHomeFLag(String homeFLag) {
        this.homeFLag = homeFLag;
    }

    public String getVisitingFlag() {
        return visitingFlag;
    }

    public void setVisitingFlag(String visitingFlag) {
        this.visitingFlag = visitingFlag;
    }
}
