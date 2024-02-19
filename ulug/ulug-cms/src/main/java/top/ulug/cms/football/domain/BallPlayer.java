package top.ulug.cms.football.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 球员
 * Created by fddiljf on 2017/1/27.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class BallPlayer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long playerId;
    private String playerName;
    private String enName;
    private String birthday;
    private String tall;
    private String weight;
    @Column(insertable = false, updatable = false)
    private Long countryId;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "countryId")
    private BallCountry country;
    @Column(insertable = false, updatable = false)
    private Long clubId;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "clubId")
    private BallClub club;
    private String position;
    private String clubNumber;
    private String countryNumber;
    private String playerValue;
    private String avatar;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String note;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getTall() {
        return tall;
    }

    public void setTall(String tall) {
        this.tall = tall;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public BallClub getClub() {
        return club;
    }

    public void setClub(BallClub club) {
        this.club = club;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getClubNumber() {
        return clubNumber;
    }

    public void setClubNumber(String clubNumber) {
        this.clubNumber = clubNumber;
    }

    public String getCountryNumber() {
        return countryNumber;
    }

    public void setCountryNumber(String countryNumber) {
        this.countryNumber = countryNumber;
    }

    public String getPlayerValue() {
        return playerValue;
    }

    public void setPlayerValue(String playerValue) {
        this.playerValue = playerValue;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
