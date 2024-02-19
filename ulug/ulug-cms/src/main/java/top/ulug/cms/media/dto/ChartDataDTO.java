package top.ulug.cms.media.dto;

import top.ulug.cms.media.domain.MediaChartData;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liujf on 2020/7/27.
 * 逝者如斯夫 不舍昼夜
 */
public class ChartDataDTO implements Serializable {
    private String chartCode;
    private String dataField;
    private float dataValue;
    private String dataType;
    private Date recordDate;

    public ChartDataDTO(){

    }

    public ChartDataDTO(String chartCode, String dataField, float dataValue, String dataType, Date recordDate) {
        this.chartCode = chartCode;
        this.dataField = dataField;
        this.dataValue = dataValue;
        this.dataType = dataType;
        this.recordDate = recordDate;
    }

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public float getDataValue() {
        return dataValue;
    }

    public void setDataValue(float dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public MediaChartData convert() {
        MediaChartData data = new MediaChartData();
        data.setDataField(this.dataField);
        data.setDataValue(this.dataValue);
        data.setDataType(this.dataType);
        data.setRecordDate(this.recordDate);
        return data;
    }
}
