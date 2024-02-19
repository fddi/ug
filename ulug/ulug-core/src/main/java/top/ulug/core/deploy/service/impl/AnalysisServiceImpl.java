package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployNode;
import top.ulug.core.deploy.repository.DeployAccessLogRepository;
import top.ulug.core.deploy.repository.DeployNodeRepository;
import top.ulug.core.deploy.service.AnalysisService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2020-09-16.
 * 逝者如斯夫 不舍昼夜
 */

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Autowired
    DeployAccessLogRepository logRepository;
    @Autowired
    DeployNodeRepository nodeRepository;

    @Override
    public WrapperDTO<Long> pvToday() {
        Calendar cr = Calendar.getInstance();
        cr.set(Calendar.HOUR_OF_DAY, 0);
        cr.set(Calendar.MINUTE, 0);
        cr.set(Calendar.SECOND, 0);
        long num = logRepository.pvToday(cr.getTime());
        return WrapperDTO.success(num);
    }

    @Override
    public WrapperDTO<Map> node() {
        List<DeployNode> list = nodeRepository.findAll();
        long total = list == null ? 0 : list.size();
        long run = 0;
        for (int i = 0; i < total; i++) {
            DeployNode node = list.get(i);
            if ("1".equals(node.getNodeStatus())) {
                run++;
            }
        }
        Map<String, Long> map = new HashMap<>();
        map.put("totalNode", total);
        map.put("runNode", run);
        return WrapperDTO.success(map);
    }

}
