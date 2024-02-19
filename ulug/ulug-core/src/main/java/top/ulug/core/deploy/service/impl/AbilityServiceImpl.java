package top.ulug.core.deploy.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.base.dto.AbilityDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployAbility;
import top.ulug.core.deploy.repository.DeployAbilityRepository;
import top.ulug.core.deploy.service.AbilityService;

import java.util.List;

/**
 * Created by liujf on 2019/3/30.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class AbilityServiceImpl implements AbilityService {
    @Value("${spring.application.name}")
    private String coreAppName;
    @Autowired
    private DeployAbilityRepository abilityRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> saveAbility(List<AbilityDTO> list, String coreName, String appName) {
        if (!coreAppName.equals(coreName)) {
            return WrapperDTO.noPermission();
        }
        List<DeployAbility> abilities = modelMapper.map(list,
                new TypeToken<List<DeployAbility>>() {
                }.getType());
        abilityRepository.updateStatus("0", appName);
        List<DeployAbility> oList = abilityRepository.findByProjectId(appName);
        for (DeployAbility ability : abilities) {
            ability.setStatus("1");
            for (DeployAbility old : oList) {
                if (ability.getAbilityUri().equals(old.getAbilityUri())) {
                    ability.setAbilityId(old.getAbilityId());
                    break;
                }
            }
        }
        abilityRepository.saveAll(abilities);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<List<DeployAbility>> list(String abilityNote) {
        abilityNote = abilityNote == null ? "" : abilityNote;
        List<DeployAbility> abilities = abilityRepository
                .findByNote(abilityNote);
        return WrapperDTO.success(abilities);
    }

    @Override
    public WrapperDTO<String> del(Long abilityId) {
        abilityRepository.deleteById(abilityId);
        return WrapperDTO.success();
    }
}
