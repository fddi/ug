package top.ulug.cms.media.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020/7/20.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaChart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chartId;

    @Column(insertable = false, updatable = false)
    private Long subjectId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "subjectId")
    private MediaSubject subject;

    @Column(unique = true, updatable = false)
    private String chartCode;

    private String chartName;

    private String chartType;

    private String dataSource;

    private String note;

    private String aliasX;

    private String aliasY;

    @Column(columnDefinition = "VARCHAR(10) COMMENT '发布状态'")
    private String publishStatus;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '区域编码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
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

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getAliasX() {
        return aliasX;
    }

    public void setAliasX(String aliasX) {
        this.aliasX = aliasX;
    }

    public String getAliasY() {
        return aliasY;
    }

    public void setAliasY(String aliasY) {
        this.aliasY = aliasY;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }
}
