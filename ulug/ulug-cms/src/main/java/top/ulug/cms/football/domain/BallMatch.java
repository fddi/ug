package top.ulug.cms.football.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020/10/26.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class BallMatch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long matchId;
    @Column(insertable = false, updatable = false)
    private Long systemId;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "systemId")
    private BallSystem system;
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
    @Column(columnDefinition = "VARCHAR(2000) COMMENT '比赛统计'")
    private String countJson;
    private String period;

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

    public BallSystem getSystem() {
        return system;
    }

    public void setSystem(BallSystem system) {
        this.system = system;
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

    public String getCountJson() {
        return countJson;
    }

    public void setCountJson(String countJson) {
        this.countJson = countJson;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
