package top.ulug.cms.media.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaChart;
import top.ulug.cms.media.domain.MediaChartData;
import top.ulug.cms.media.dto.ChartDTO;
import top.ulug.cms.media.dto.ChartDataDTO;
import top.ulug.cms.media.dto.ChartDataSetDTO;
import top.ulug.cms.media.repository.MediaChartDataRepository;
import top.ulug.cms.media.repository.MediaChartRepository;
import top.ulug.cms.media.service.ChartDataService;
import top.ulug.core.api.service.AuthService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020/7/26.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class ChartDataServiceImpl implements ChartDataService {
    @Autowired
    MediaChartRepository chartRepository;
    @Autowired
    MediaChartDataRepository dataRepository;
    @Autowired
    AuthService authService;

    @Override
    public WrapperDTO<String> save(MediaChartData chartData) {
        Long chartId = chartData.getChartId();
        Optional<MediaChart> op = chartRepository.findById(chartId);
        if (!op.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "MediaChart");
        }
        chartData.setAreaCode(op.get().getAreaCode());
        chartData.setUnitCode(op.get().getUnitCode());
        chartData.setChart(op.get());
        dataRepository.save(chartData);
        return WrapperDTO.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception {
        //同步读取,自动finish
        List<Object> list = EasyExcel.read(file.getInputStream())
                .head(ChartDataDTO.class).sheet().doReadSync();
        List<MediaChartData> saveList = new ArrayList<>();
        MediaChart chart = null;
        for (int i = 0; i < list.size(); i++) {
            ChartDataDTO data = (ChartDataDTO) list.get(i);
            if (StringUtils.isEmpty(data.getChartCode()) || StringUtils.isEmpty(data.getDataField())) {
                throw new Exception(String.format("第%d行出错：必填项存在空值，请修改！", i + 1));
            }
            chart = chartRepository.findByChartCode(data.getChartCode());
            if (chart == null &&
                    (chart = chartRepository.findByChartCode(data.getChartCode())) == null) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "MediaChart");
            }
            if (!authService.checkDevOps()
                    && !authService.checkUnit(chart.getUnitCode())) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
            }
            MediaChartData chartData = data.convert();
            chartData.setChart(chart);
            chartData.setUnitCode(chart.getUnitCode());
            chartData.setAreaCode(chart.getAreaCode());
            saveList.add(chartData);
        }
        assert chart != null;
        dataRepository.deleteByChartId(chart.getChartId());
        dataRepository.saveAll(saveList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<ChartDataSetDTO> data(String chartCode, String startDate, String endDate) {
        MediaChart chart = chartRepository.findByChartCode(chartCode);
        if (chart == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
        }
        List<ChartDataDTO> list = new ArrayList<>();
        if (!StringUtils.isEmpty(chartCode) && StringUtils.isEmpty(chartCode)) {
            Date sd = DateTimeUtils.getDate(startDate, "yyyy-MM-dd HH:mm:ss");
            Date ed = DateTimeUtils.getDate(endDate, "yyyy-MM-dd HH:mm:ss");
            list = dataRepository.findByChartCodeAndRecordDateBetween(chartCode, sd, ed);
        } else {
            list = dataRepository.findByChartCode(chartCode);
        }
        ChartDataSetDTO setDTO = new ChartDataSetDTO();
        setDTO.setChart(JSON.parseObject(JSON.toJSONString(chart), ChartDTO.class));
        setDTO.setData(list);
        return WrapperDTO.success(setDTO);
    }

}
