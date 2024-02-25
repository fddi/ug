package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.repository.DeployAccessLogRepository;
import top.ulug.core.deploy.service.AnalysisService;

import java.util.Calendar;

/**
 * Created by liujf on 2020-09-16.
 * 逝者如斯夫 不舍昼夜
 */

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Autowired
    DeployAccessLogRepository logRepository;

    @Override
    public WrapperDTO<Long> pvToday() {
        Calendar cr = Calendar.getInstance();
        cr.set(Calendar.HOUR_OF_DAY, 0);
        cr.set(Calendar.MINUTE, 0);
        cr.set(Calendar.SECOND, 0);
        long num = logRepository.pvToday(cr.getTime());
        return WrapperDTO.success(num);
    }

}
