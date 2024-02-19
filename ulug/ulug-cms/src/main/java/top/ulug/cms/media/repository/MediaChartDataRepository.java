package top.ulug.cms.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.media.domain.MediaChartData;
import top.ulug.cms.media.dto.ChartDataDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by liujf on 2020/7/20.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaChartDataRepository extends JpaRepository<MediaChartData, Long>,
        JpaSpecificationExecutor<MediaChartData> {

    void deleteByChartId(Long chartId);

    @Query("select new top.ulug.cms.media.dto.ChartDataDTO(t.chart.chartCode,dataField,dataValue,dataType,recordDate) from MediaChartData t where t.chart.chartCode = ?1 ")
    List<ChartDataDTO> findByChartCode(String chartCode);

    @Query("select new top.ulug.cms.media.dto.ChartDataDTO(t.chart.chartCode,dataField,dataValue,dataType,recordDate) from MediaChartData t where t.chart.chartCode = ?1 and recordDate between ?2 and ?3")
    List<ChartDataDTO> findByChartCodeAndRecordDateBetween(String chartCode, Date startDate, Date endDate);
}
