package top.ulug.cms.football.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 国家
 * Created by fddiljf on 2017/2/3.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class BallCountry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long countryId;
    @Column(unique = true, nullable = false)
    private String countryName;
    @Column(unique = true)
    private String enName;
    private String continent;
    private String capital;
    private String flag;
    private int nowRanking;
    private int nowPoints;
    private String coach;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String note;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getNowRanking() {
        return nowRanking;
    }

    public void setNowRanking(int nowRanking) {
        this.nowRanking = nowRanking;
    }

    public int getNowPoints() {
        return nowPoints;
    }

    public void setNowPoints(int nowPoints) {
        this.nowPoints = nowPoints;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
