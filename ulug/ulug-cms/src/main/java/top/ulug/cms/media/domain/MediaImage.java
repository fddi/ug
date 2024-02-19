package top.ulug.cms.media.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020/2/21.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaImage extends BaseEntity {

    @Id
    @Column(columnDefinition = "VARCHAR(40) COMMENT '图片序列'")
    private String imgKey;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String imgData;

    @Column(columnDefinition = "VARCHAR(20) COMMENT '图片类型'")
    private String imgType;

    @Column(columnDefinition = "VARCHAR(128) COMMENT '图片标签'")
    private String imgTags;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '图片说明'")
    private String imgNote;

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getImgData() {
        return imgData;
    }

    public void setImgData(String imgData) {
        this.imgData = imgData;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgTags() {
        return imgTags;
    }

    public void setImgTags(String imgTags) {
        this.imgTags = imgTags;
    }

    public String getImgNote() {
        return imgNote;
    }

    public void setImgNote(String imgNote) {
        this.imgNote = imgNote;
    }
}
