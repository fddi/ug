package top.ulug.cms.media.dto;

import java.util.List;

/**
 * Created by liujf on 2020/8/2.
 * 逝者如斯夫 不舍昼夜
 */
public class ChartDataSetDTO {
    private ChartDTO chart;
    private List<ChartDataDTO> data;

    public ChartDTO getChart() {
        return chart;
    }

    public void setChart(ChartDTO chart) {
        this.chart = chart;
    }

    public List<ChartDataDTO> getData() {
        return data;
    }

    public void setData(List<ChartDataDTO> data) {
        this.data = data;
    }
}
