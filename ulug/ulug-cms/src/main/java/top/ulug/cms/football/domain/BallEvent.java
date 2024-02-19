package top.ulug.cms.football.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by fddiljf on 2017/4/11.
 * 逝者如斯夫 不舍昼夜
 */

@Entity
@Table
public class BallEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;
    @Column(insertable = false, updatable = false)
    private Long matchId;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "matchId")
    private BallMatch match;
    @Column(insertable = false, updatable = false)
    private Long playerId;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "playerId")
    private BallPlayer player;
    private String positionId;
    private String eventTime;
    private String eventCode;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String note;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public BallMatch getMatch() {
        return match;
    }

    public void setMatch(BallMatch match) {
        this.match = match;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public BallPlayer getPlayer() {
        return player;
    }

    public void setPlayer(BallPlayer player) {
        this.player = player;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
