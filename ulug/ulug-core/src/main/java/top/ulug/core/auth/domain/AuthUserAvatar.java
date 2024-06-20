package top.ulug.core.auth.domain;

import jakarta.persistence.*;
import top.ulug.base.auditor.BaseEntity;

/**
 * Created by liujf on 2020/2/21.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class AuthUserAvatar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long avatarId;

    @Column(unique = true, nullable = false, length = 10)
    private String userName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String avatarData;

    @Column(nullable = false, length = 10)
    private String fileType;

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(String avatarData) {
        this.avatarData = avatarData;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
