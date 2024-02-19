package top.ulug.cms.media.dto;

import java.io.Serializable;

/**
 * Created by liujf on 2020/7/26.
 * 逝者如斯夫 不舍昼夜
 */
public class ChartDTO implements Serializable {
    private Long chartId;
    private Long subjectId;
    private String chartCode;
    private String chartName;
    private String chartType;
    private String dataSource;
    private String note;
    private String aliasX;
    private String aliasY;
    private String publishStatus;

    public ChartDTO(Long chartId, Long subjectId, String chartCode, String chartName, String chartType, String dataSource, String note, String aliasX, String aliasY, String publishStatus) {
        this.chartId = chartId;
        this.subjectId = subjectId;
        this.chartCode = chartCode;
        this.chartName = chartName;
        this.chartType = chartType;
        this.dataSource = dataSource;
        this.note = note;
        this.aliasX = aliasX;
        this.aliasY = aliasY;
        this.publishStatus = publishStatus;
    }

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
