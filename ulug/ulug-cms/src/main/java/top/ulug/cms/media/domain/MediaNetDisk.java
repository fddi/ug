package top.ulug.cms.media.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaNetDisk extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long diskId;

    @Column(insertable = false, updatable = false)
    private Long subjectId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subjectId")
    private MediaSubject subject;

    @Column(nullable = false, columnDefinition = "VARCHAR(128) COMMENT '云盘名称'")
    private String diskName;

    @Column(nullable = false)
    private long diskSize;

    @Column(nullable = false)
    private long usedSize;

    private String location;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '区域编码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    @Column(columnDefinition = "VARCHAR(10) COMMENT '默认网盘 1--是 其他 否'")
    private String defaultFlag;

    @Column(nullable = false, columnDefinition = "VARCHAR(2) COMMENT '状态 1--可用 0--禁用'")
    private String status;

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
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

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(long diskSize) {
        this.diskSize = diskSize;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(long usedSize) {
        this.usedSize = usedSize;
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

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
