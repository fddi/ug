package top.ulug.cms.media.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 主体
 * Created by liujf on 2019/4/15.
 * 逝者如斯夫 不舍昼夜
 */

@Entity
@Table
public class MediaSubject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subjectId;

    @Column(unique = true, nullable = false,
            columnDefinition = "VARCHAR(40) COMMENT '业务号秘钥'")
    private String subjectKey;

    @Column(unique = true, nullable = false,
            columnDefinition = "VARCHAR(64) COMMENT '业务号代码'")
    private String subjectCode;

    @Column(unique = true, nullable = false,
            columnDefinition = "VARCHAR(64) COMMENT '业务号名称'")
    private String subjectName;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '业务号描述'")
    private String subjectNote;

    @Column(columnDefinition = "VARCHAR(20) COMMENT '业务号类别'")
    private String subjectType;

    @Column(columnDefinition = "VARCHAR(40) COMMENT '图标-文件序列'")
    private String logo;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '区域编码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    @Column(columnDefinition = "VARCHAR(2) COMMENT '状态 1--正常 0--异常'")
    private String status;

    private Integer sort;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectKey() {
        return subjectKey;
    }

    public void setSubjectKey(String subjectKey) {
        this.subjectKey = subjectKey;
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

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
