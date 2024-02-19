package top.ulug.cms.media.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.cms.media.domain.MediaChart;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.ChartDTO;
import top.ulug.cms.media.repository.MediaChartDataRepository;
import top.ulug.cms.media.repository.MediaChartRepository;
import top.ulug.cms.media.repository.MediaSubjectRepository;
import top.ulug.cms.media.service.ChartService;
import top.ulug.core.api.service.AuthService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020/7/26.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class ChartServiceImpl implements ChartService {
    @Autowired
    MediaSubjectRepository subjectRepository;
    @Autowired
    AuthService authService;
    @Autowired
    MediaChartRepository chartRepository;
    @Autowired
    MediaChartDataRepository dataRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<String> save(MediaChart... charts) throws Exception {
        List<MediaChart> list = Arrays.asList(charts);
        for (MediaChart chart : list) {
            Optional<MediaSubject> op = subjectRepository.findById(chart.getSubjectId());
            if (!op.isPresent()) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "MediaSubject");
            }
            chart.setAreaCode(op.get().getAreaCode());
            chart.setUnitCode(op.get().getUnitCode());
            chart.setSubject(op.get());
        }
        chartRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> del(MediaChart... charts) {
        List<MediaChart> list = Arrays.asList(charts);
        for (MediaChart chart : list) {
            Optional<MediaChart> opl = chartRepository.findById(chart.getChartId());
            if (!opl.isPresent() || !authService.checkDevOps()
                    && !authService.checkUnit(opl.get().getUnitCode())) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
            }
            dataRepository.deleteByChartId(chart.getChartId());
        }
        chartRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<ChartDTO> findOne(Long id) {
        Optional<MediaChart> optional = chartRepository.findById(id);
        if (!optional.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        return WrapperDTO.success(
                modelMapper.map(optional.get(), ChartDTO.class));
    }

    @Override
    public WrapperDTO<PageDTO<ChartDTO>> findPage(int pageSize, int pageNo, MediaChart chart) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String name = chart.getChartName() == null ? "" : chart.getChartName();
        Page<ChartDTO> page = chartRepository.page(chart.getSubjectId(),
                name, pageable);
        return WrapperDTO.success(new PageDTO<ChartDTO>().convert(page));
    }

}
