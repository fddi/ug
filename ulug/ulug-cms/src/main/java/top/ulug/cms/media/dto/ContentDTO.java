package top.ulug.cms.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by liujf on 2020/2/27.
 * 逝者如斯夫 不舍昼夜
 */
public class ContentDTO {
    private Long contentId;
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private String subjectNote;
    private String contentType;
    private String contentTitle;
    private String signer;
    private String cover;
    private String tags;
    private String summary;
    private String article;
    private String publishStatus;
    private Integer sort;
    private String annexes;
    private String logo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;

    public  ContentDTO(){

    }
    public ContentDTO(String subjectCode, String subjectName, String subjectNote, String contentType, String contentTitle, String signer, String tags, String summary, String article, String logo, Date dateTime) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectNote = subjectNote;
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.signer = signer;
        this.tags = tags;
        this.summary = summary;
        this.article = article;
        this.logo = logo;
        this.dateTime = dateTime;
    }

    public ContentDTO(Long contentId, String subjectCode, String subjectName, String contentType, String contentTitle, String signer, String cover, String tags, String summary, Date dateTime, String logo, String publishStatus, Integer sort) {
        this.contentId = contentId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.contentType = contentType;
        this.contentTitle = contentTitle;
        this.signer = signer;
        this.cover = cover;
        this.tags = tags;
        this.summary = summary;
        this.dateTime = dateTime;
        this.logo = logo;
        this.publishStatus = publishStatus;
        this.sort = sort;
    }

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

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectNote() {
        return subjectNote;
    }

    public void setSubjectNote(String subjectNote) {
        this.subjectNote = subjectNote;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
