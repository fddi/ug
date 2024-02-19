package top.ulug.cms.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.media.domain.MediaChart;
import top.ulug.cms.media.dto.ChartDTO;

/**
 * Created by liujf on 2020/7/20.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaChartRepository extends JpaRepository<MediaChart, Long>,
        JpaSpecificationExecutor<MediaChart> {

    @Query("select new top.ulug.cms.media.dto.ChartDTO(chartId,subjectId,chartCode,chartName,chartType,dataSource,note,aliasX,aliasY,publishStatus) from MediaChart t where t.subjectId = ?1 and t.chartName like %?2%")
    Page<ChartDTO> page(Long subjectId, String name, Pageable page);

    MediaChart findByChartCode(String chartCode);

}
