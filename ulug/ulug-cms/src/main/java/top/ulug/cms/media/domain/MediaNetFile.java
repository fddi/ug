package top.ulug.cms.media.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaNetFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fileId;

    @Column(insertable = false, updatable = false)
    private Long diskId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "diskId")
    private MediaNetDisk netDisk;

    private String fileName;

    private String fileExt;

    private String checkCode;

    private String uri;

    private String fileTags;

    private String fileNote;

    private String fileType;

    private Long fileSize;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
    }

    public MediaNetDisk getNetDisk() {
        return netDisk;
    }

    public void setNetDisk(MediaNetDisk netDisk) {
        this.netDisk = netDisk;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileTags() {
        return fileTags;
    }

    public void setFileTags(String fileTags) {
        this.fileTags = fileTags;
    }

    public String getFileNote() {
        return fileNote;
    }

    public void setFileNote(String fileNote) {
        this.fileNote = fileNote;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

}
