package top.ulug.cms.media.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 媒体内容
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contentId;

    @Column(insertable = false, updatable = false)
    private Long subjectId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subjectId")
    private MediaSubject subject;

    @Column(columnDefinition = "VARCHAR(10) COMMENT '内容分类'")
    private String contentType;

    @Column(nullable = false, columnDefinition = "VARCHAR(128) COMMENT '内容标题'")
    private String contentTitle;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '署名'")
    private String signer;

    @Column(columnDefinition = "VARCHAR(40) COMMENT '封面图片'")
    private String cover;

    @Column(columnDefinition = "VARCHAR(128) COMMENT '标签'")
    private String tags;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '摘要'")
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String article;

    @Column(columnDefinition = "VARCHAR(10) COMMENT '发布状态'")
    private String publishStatus;

    @Column(columnDefinition = "INT(5) COMMENT '排序'")
    private Integer sort;

    @Column(columnDefinition = "VARCHAR(128) COMMENT '附件列表'")
    private String annexes;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '区域编码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public MediaSubject getSubject() {
        return subject;
    }

    public void setSubject(MediaSubject subject) {
        this.subject = subject;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getAnnexes() {
        return annexes;
    }

    public void setAnnexes(String annexes) {
        this.annexes = annexes;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
