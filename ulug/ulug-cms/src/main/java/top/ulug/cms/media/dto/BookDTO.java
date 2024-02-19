package top.ulug.cms.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by liujf on 2020/2/27.
 * 逝者如斯夫 不舍昼夜
 */
public class BookDTO {
    private Long bookId;
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private String subjectNote;
    private String title;
    private String tags;
    private String catalog;
    private String notes;
    private String publishStatus;
    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;

    public BookDTO(String subjectCode, String subjectName, String subjectNote, String title, String tags, String catalog, String notes, Date dateTime) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectNote = subjectNote;
        this.title = title;
        this.tags = tags;
        this.catalog = catalog;
        this.notes = notes;
        this.dateTime = dateTime;
    }

    public BookDTO(Long bookId, Long subjectId, String title, String tags, String publishStatus, Integer sort, Date dateTime) {
        this.bookId = bookId;
        this.subjectId = subjectId;
        this.title = title;
        this.tags = tags;
        this.publishStatus = publishStatus;
        this.sort = sort;
        this.dateTime = dateTime;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
