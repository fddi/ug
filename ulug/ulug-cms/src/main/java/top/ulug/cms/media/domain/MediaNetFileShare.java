package top.ulug.cms.media.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020-06-10.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaNetFileShare extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shareId;

    @Column(insertable = false, updatable = false)
    private Long fileId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "fileId")
    private MediaNetFile file;

    @Column(unique = true, nullable = false)
    private String shareKey;

    private String shareType;

    private Integer shareDays;

    private String shareCode;

    @Column(nullable = false)
    private String shareUri;

    private String status;

    private Long visits;

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public Integer getShareDays() {
        return shareDays;
    }

    public void setShareDays(Integer shareDays) {
        this.shareDays = shareDays;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getShareUri() {
        return shareUri;
    }

    public void setShareUri(String shareUri) {
        this.shareUri = shareUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public MediaNetFile getFile() {
        return file;
    }

    public void setFile(MediaNetFile file) {
        this.file = file;
    }
}
