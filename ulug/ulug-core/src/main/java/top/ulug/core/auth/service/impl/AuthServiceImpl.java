package top.ulug.core.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.JwtUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthMenu;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.repository.AuthRoleRepository;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.deploy.domain.DeployAbility;
import top.ulug.core.deploy.repository.DeployAbilityRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujf on 2019/3/31.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${project.auth.active.time}")
    private Long activeTime;
    @Value("${project.auth.developer.code}")
    private String developerCode;
    @Resource(name = "redisTemplate")
    ValueOperations<String, AuthDTO> redisVoAuth;
    @Autowired
    AuthRoleRepository roleRepository;
    @Autowired
    DeployAbilityRepository abilityRepository;
    @Autowired
    RequestUtils requestUtils;

    @Override
    public boolean checkToken() {
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            JwtUtils.pares(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return false;
        }
        if (!"1".equals(authDTO.getAccount().getStatus())) {
            return false;
        }
        long time = new Date().getTime();
        if ((authDTO.getAccount().getAuthTime() - time) / 1000 > activeTime) {
            return false;
        }
        authDTO.getAccount().setAuthTime(time);
        redisVoAuth.set(token, authDTO, activeTime, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public boolean authAbility(String uri) {
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return false;
        }
        if (this.checkDevOps()) {
            //开发账号不用鉴权
            return true;
        }
        List<DeployAbility> abilityList = authDTO.getAbilityList();
        if (abilityList == null) {
            abilityList = this.listUserAbility();
        }
        authDTO.setAbilityList(abilityList);
        redisVoAuth.set(token, authDTO, activeTime, TimeUnit.MINUTES);
        for (DeployAbility owner : abilityList) {
            if (owner.getAbilityUri().equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkDevOps() {
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return false;
        }
        if (developerCode.equals(authDTO.getAccount().getUserType())) {
            return true;
        }
        return false;
    }

    @Override
    public List<DeployAbility> listUserAbility() {
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return null;
        }
        List<DeployAbility> abilityList;
        if (this.checkDevOps()) {
            //开发账号不用鉴权
            return abilityRepository.findByAbilityUriNotLikeOrderByAbilityNote("%dev%");
        }
        List<AuthRole> inRoles = roleRepository.findAllByUserList_UserName(
                authDTO.getAccount().getUserName());
        abilityList = new ArrayList<>();
        List<AuthMenu> ownerMenu = new ArrayList<>();
        for (AuthRole role : inRoles) {
            if (role.getMenuList() == null) {
                continue;
            }
            ownerMenu.removeAll(role.getMenuList());
            ownerMenu.addAll(role.getMenuList());
        }
        for (AuthMenu menu : ownerMenu) {
            List<DeployAbility> abilities = menu.getAbilityList();
            abilityList.removeAll(abilities);
            abilityList.addAll(abilities);
        }
        return abilityList;
    }

    @Override
    public boolean checkAreaCode(String areaCode) {
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return false;
        }
        String userArea = authDTO.getAccount().getAreaCode();
        if (areaCode.contains(userArea)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUnitCode(String unitCode) {
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null
                || StringUtils.isEmpty(unitCode)) {
            return false;
        }
        String userUnitCode = authDTO.getAccount().getUnitCode();
        return userUnitCode.equals(unitCode);
    }

    @Override
    public WrapperDTO<AccountDTO> checkAccount() {
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return WrapperDTO.npe("user");
        }
        return WrapperDTO.success(authDTO.getAccount());
    }

    @Override
    public void refreshLoginTime() {
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return;
        }
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return;
        }
        redisVoAuth.set(token, authDTO, activeTime, TimeUnit.MINUTES);
    }

}
