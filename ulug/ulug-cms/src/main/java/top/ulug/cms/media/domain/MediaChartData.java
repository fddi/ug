package top.ulug.cms.media.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liujf on 2020-07-17.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class MediaChartData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dataId;

    @Column(insertable = false, updatable = false)
    private Long chartId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chartId")
    private MediaChart chart;

    private String dataField;

    private String dataType;

    @Column(precision = 15, scale = 2)
    private float dataValue;

    private String linkCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordDate;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String data;

    @Column(columnDefinition = "VARCHAR(32) COMMENT '区域编码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public MediaChart getChart() {
        return chart;
    }

    public void setChart(MediaChart chart) {
        this.chart = chart;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public float getDataValue() {
        return dataValue;
    }

    public void setDataValue(float dataValue) {
        this.dataValue = dataValue;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
}
