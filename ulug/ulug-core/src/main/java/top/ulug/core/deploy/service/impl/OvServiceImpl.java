package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.deploy.domain.DeployOv;
import top.ulug.core.deploy.repository.DeployOvRepository;
import top.ulug.core.deploy.service.CacheService;
import top.ulug.core.deploy.service.OvService;
import top.ulug.base.dto.PageDTO;

import jakarta.annotation.Resource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liujf on 2020/2/9.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class OvServiceImpl implements OvService {
    //顶级机构代码
    public static final String TAG_TOP_UNIT_CODE = "Y";

    @Autowired
    DeployOvRepository ovRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private RequestUtils requestUtils;
    @Autowired
    CacheService cacheService;

    @Override
    public String getOv(String optionCode) {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return null;
        }
        DeployOv option = ovRepository.findByUnitCodeAndOptionCode(
                authDTO.getAccount().getUnitCode(), optionCode);
        if (option == null) {
            option = ovRepository.findByUnitCodeAndOptionCode(
                    TAG_TOP_UNIT_CODE, optionCode);
        }
        if (option != null) {
            return option.getOptionValue();
        }
        return null;
    }

    @Override
    public String getPublicOv(String unitCode, String optionCode) {
        if (StringUtils.isEmpty(unitCode) || StringUtils.isEmpty(optionCode)) {
            return null;
        }
        DeployOv option = ovRepository.findByUnitCodeAndOptionCode(unitCode, optionCode);
        if (option == null) {
            option = ovRepository.findByUnitCodeAndOptionCode(
                    TAG_TOP_UNIT_CODE, optionCode);
        }
        if (option != null) {
            return option.getOptionValue();
        }
        return null;
    }

    @Override
    public boolean saveOv(String optionCode, String value) {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return false;
        }
        DeployOv option = ovRepository.findByUnitCodeAndOptionCode(
                authDTO.getAccount().getUnitCode(), optionCode);
        if (option == null) {
            option = new DeployOv();
            option.setUnitCode(authDTO.getAccount().getUnitCode());
        } else {
            if (value.equals(option.getOptionValue())) {
                return false;
            }
        }
        option.setOptionCode(optionCode);
        option.setOptionValue(value);
        ovRepository.save(option);
        return true;
    }

    @Override
    public WrapperDTO<List<DeployOv>> listOv(String optionCode) {
        List<DeployOv> list;
        if (authService.checkDevOps()) {
            //开发者,返回所有配置值
            list = ovRepository.findByOptionCode(optionCode);
        } else {
            String appId = requestUtils.getCurrentAppId();
            String token = requestUtils.getCurrentToken();
            AuthDTO authDTO = cacheService.getAuth(appId, token);
            if (authDTO == null || authDTO.getAccount() == null) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "authDTO");
            }
            list = ovRepository.findValues(
                    optionCode, authDTO.getAccount().getUnitCode());
        }
        return WrapperDTO.success(list);
    }

    @Override
    public WrapperDTO<String> save(DeployOv... ovs) {
        List<DeployOv> list = Arrays.asList(ovs);
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        assert authDTO != null;
        for (DeployOv ov : list) {
            if (StringUtils.isEmpty(ov.getUnitCode())) {
                ov.setUnitCode(authDTO.getAccount().getUnitCode());
            } else if (!authService.checkDevOps()
                    && !authService.checkUnitCode(ov.getUnitCode())) {
                //非开发者禁止保存非本单位数据
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
            }
        }
        ovRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(DeployOv... ovs) {
        List<DeployOv> list = Arrays.asList(ovs);
        if (!authService.checkDevOps()) {
            //非开发者禁止删除数据
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        ovRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<DeployOv> findOne(Long id) {
        return null;
    }

    @Override
    public WrapperDTO<PageDTO<DeployOv>> findPage(int pageSize, int pageNo, DeployOv deployOv) {
        return null;
    }
}
