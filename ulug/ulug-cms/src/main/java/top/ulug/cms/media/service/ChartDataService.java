package top.ulug.cms.media.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.domain.MediaChartData;
import top.ulug.cms.media.dto.ChartDataSetDTO;

/**
 * Created by liujf on 2020/7/26.
 * 逝者如斯夫 不舍昼夜
 */
public interface ChartDataService {

    WrapperDTO<String> save(MediaChartData chartData);

    WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception;

    WrapperDTO<ChartDataSetDTO> data(String chartCode, String startDate, String endDate);
}
