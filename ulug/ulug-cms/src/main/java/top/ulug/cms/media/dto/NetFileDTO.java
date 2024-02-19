package top.ulug.cms.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liujf on 2020/5/31.
 * 逝者如斯夫 不舍昼夜
 */
public class NetFileDTO implements Serializable {
    private Long fileId;
    private Long diskId;
    private String fileName;
    private String fileExt;
    private String checkCode;
    private String uri;
    private String fileType;
    private Long fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    public NetFileDTO(Long fileId, Long diskId, String fileName, String fileExt, String checkCode, String uri, String fileType, Long fileSize, Date gmtCreate) {
        this.fileId = fileId;
        this.diskId = diskId;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.checkCode = checkCode;
        this.uri = uri;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.gmtCreate = gmtCreate;
    }

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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
