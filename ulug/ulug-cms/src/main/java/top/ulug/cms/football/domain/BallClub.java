package top.ulug.cms.football.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 俱乐部
 * Created by fddiljf on 2017/2/3.
 * 逝者如斯夫 不舍昼夜
 */

@Entity
@Table
public class BallClub extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clubId;
    @Column(unique = true, nullable = false)
    private String clubName;
    private String enName;
    private String fullName;
    @Column(insertable = false, updatable = false)
    private Long countryId;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "countryId")
    private BallCountry country;
    private String city;
    private String buildTime;
    private String stadium;
    private String flag;
    private String coach;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String note;
    private String magicCode;

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public BallCountry getCountry() {
        return country;
    }

    public void setCountry(BallCountry country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getMagicCode() {
        return magicCode;
    }

    public void setMagicCode(String magicCode) {
        this.magicCode = magicCode;
    }
}
