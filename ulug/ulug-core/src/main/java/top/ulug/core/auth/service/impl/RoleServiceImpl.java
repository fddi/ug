package top.ulug.core.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthMenu;
import top.ulug.core.auth.domain.AuthOrg;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.dto.RoleDTO;
import top.ulug.core.auth.dto.RoleOrgDTO;
import top.ulug.core.auth.repository.AuthMenuRepository;
import top.ulug.core.auth.repository.AuthOrgRepository;
import top.ulug.core.auth.repository.AuthRoleRepository;
import top.ulug.core.auth.repository.AuthUserRepository;
import top.ulug.core.auth.service.AccountService;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.auth.service.MenuService;
import top.ulug.core.auth.service.RoleService;
import top.ulug.core.deploy.repository.DeployAbilityRepository;
import top.ulug.base.dto.PageDTO;

import jakarta.annotation.Resource;
import top.ulug.core.deploy.service.CacheService;

import java.util.*;

/**
 * Created by liujf on 2019/4/2.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthRoleRepository roleRepository;
    @Autowired
    private AuthUserRepository userRepository;
    @Autowired
    private DeployAbilityRepository abilityRepository;
    @Autowired
    private AuthMenuRepository menuRepository;
    @Autowired
    private AuthOrgRepository orgRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private MenuService menuService;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    CacheService cacheService;

    @Override
    public WrapperDTO<String> roleCopy(Long orgId, String roleIds) {
        Optional<AuthOrg> op = orgRepository.findById(orgId);
        if (!op.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthOrg");
        }
        if (!authService.checkDevOps() &&
                (!authService.checkUnitCode(op.get().getUnitCode()))) {
            //非开发者禁止保存非本单位数据
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
        }
        String[] tmp = roleIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String s : tmp) {
            ids.add(Long.parseLong(s));
        }
        List<AuthRole> roleList = roleRepository.findByRoleIdIn(ids);
        List<AuthRole> saveList = new ArrayList<>();
        for (AuthRole role : roleList) {
            AuthRole newRole = new AuthRole();
            newRole.setAreaCode(op.get().getAreaCode());
            newRole.setUnitCode(op.get().getUnitCode());
            newRole.setOrg(op.get());
            newRole.setRoleName(role.getRoleName());
            String copyNote = StringUtils.isEmpty(role.getRoleNote()) ?
                    "复制岗位:" + role.getRoleName() : role.getRoleNote();
            newRole.setRoleNote(copyNote);
            newRole.setRoleType(role.getRoleType());
            newRole.setMenuList(role.getMenuList());
            saveList.add(newRole);
        }
        roleRepository.saveAll(saveList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<List<LabelDTO>> listRoleUser(String unitCode, Long roleId) {
        //已选中的用户列表
        Optional<AuthRole> optional = roleRepository.findById(roleId);
        if (!optional.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthRole");
        }
        AuthRole role = optional.get();
        List<AuthUser> listRight = role.getUserList();
        listRight = new ArrayList<>(listRight);
        List<LabelDTO> list = new ArrayList<>();
        for (AuthUser user : listRight) {
            LabelDTO labelDTO = new LabelDTO();
            labelDTO.setKey(String.valueOf(user.getUserId()));
            labelDTO.setTitle(user.getNickName() + "(" +
                    user.getUserName() + ")-" + user.getUnitCode());
            labelDTO.setChecked(true);
            list.add(labelDTO);
        }

        //待选择用户列表
        if (StringUtils.isEmpty(unitCode) || !authService.checkDevOps()) {
            unitCode = role.getUnitCode();
        }
        List<AuthUser> listLeft = userRepository.findByUnitCodeAndStatus(unitCode, "1");
        listLeft = new ArrayList<>(listLeft);
        if (listLeft.size() > 0) {
            listLeft.removeAll(listRight);
        }
        for (AuthUser user : listLeft) {
            LabelDTO labelDTO = new LabelDTO();
            labelDTO.setKey(String.valueOf(user.getUserId()));
            labelDTO.setTitle(user.getNickName() + "(" +
                    user.getUserName() + ")-" + user.getUnitCode());
            labelDTO.setChecked(false);
            list.add(labelDTO);
        }
        return WrapperDTO.success(list);
    }

    @Override
    public WrapperDTO<String> saveRoleUser(Long roleId, String keys) {
        String[] tmp = keys == null ? new String[]{} : keys.split(",");
        List<Long> ids = new ArrayList<>();
        for (String s : tmp) {
            ids.add(Long.valueOf(s));
        }
        List<AuthUser> users = userRepository.findByUserIdIn(ids);
        AuthRole role = roleRepository.findById(roleId).get();
        if (!authService.checkDevOps()) {
            //非开发者，不保存其他机构用户
            users.removeIf(user -> !role.getUnitCode().equals(user.getUnitCode()));
        }
        role.getUserList().clear();
        role.setUserList(users);
        roleRepository.save(role);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<TreeDTO> listRoleMenu(Long roleId, String clientName) {
        //已选中的接口列表
        AuthRole role = roleRepository.findById(roleId).get();
        List<AuthMenu> listRight = role.getMenuList();
        List<AuthMenu> list = new ArrayList<>();
        for (AuthMenu menu : listRight) {
            if ("1".equals(menu.getStatus())) {
                list.add(menu);
            }
        }
        //待选择接口列表
        List<AuthMenu> listLeft = menuService.listPersonalMenu(clientName);
        listLeft.removeAll(list);
        for (AuthMenu menu : listLeft) {
            //过滤开发者菜单
            if (menu.getMenuUri() != null && menu.getMenuUri().contains("/dev-")) {
                continue;
            }
            //暂存未选中状态
            menu.setStatus("0");
            list.add(menu);
        }
        Collections.sort(list);
        List<TreeDTO> treeDTOList = new ArrayList<>();
        for (AuthMenu menu : list) {
            if (!clientName.equals(menu.getClientName())) {
                continue;
            }
            boolean checked = "1".equals(menu.getStatus());
            treeDTOList.add(new TreeDTO(menu.getMenuId(), menu.getParentId(), String.valueOf(menu.getMenuId()),
                    menu.getMenuUri(), menu.getMenuName(), menu.getMenuName(),
                    menu.getMenuSort(), menu.getIcon(), menu.getMenuNote(), menu.getStatus(), checked, menu.getMenuType()));
        }
        TreeDTO root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                -1, "", "", "", false, "");
        root.findChild(root, treeDTOList);
        return WrapperDTO.success(root);
    }

    @Override
    public WrapperDTO<String> saveRoleMenu(Long roleId, String clientName, String keys) {
        AuthRole role = roleRepository.findById(roleId).get();
        if (StringUtils.isEmpty(keys)) {
            List<AuthMenu> menus = role.getMenuList();
            if (menus == null) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthMenu");
            }
            menus.removeIf(menu -> clientName.equals(menu.getClientName()));
            roleRepository.save(role);
            return WrapperDTO.success();
        }
        String[] tmp = keys.split(",");
        List<Long> ids = new ArrayList<>();
        for (String s : tmp) {
            ids.add(Long.valueOf(s));
        }
        List<AuthMenu> menus = menuRepository.findByMenuIdIn(ids);
        List<AuthMenu> cMenus = role.getMenuList();
        if (cMenus != null) {
            cMenus.removeIf(menu -> clientName.equals(menu.getClientName()));
            menus.addAll(cMenus);
        }
        role.setMenuList(menus);
        roleRepository.save(role);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<RoleOrgDTO> listRoleOrg(Long roleId) {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_OUT_TIME, null);
        }
        //已选中的列表
        AuthRole role = roleRepository.findById(roleId).get();
        List<AuthOrg> listRight = role.getOrgList();
        List<AuthOrg> list = new ArrayList<>();
        List<TreeDTO> unitList = new ArrayList<>();
        for (AuthOrg org : listRight) {
            if ("1".equals(org.getStatus())) {
                if (org.getUnitCode().equals(role.getUnitCode())) {
                    list.add(org);
                } else {
                    TreeDTO tmp = new TreeDTO();
                    tmp.setValue(org.getOrgCode());
                    tmp.setTitle(org.getOrgName());
                    tmp.setId(org.getOrgId());
                    unitList.add(tmp);
                }
            }
        }
        String unitCode = authService.checkDevOps() ? role.getUnitCode()
                : authDTO.getAccount().getUnitCode();
        //待选择列表
        List<AuthOrg> listLeft = orgRepository.findActive(unitCode);
        listLeft.removeAll(listRight);
        for (AuthOrg org : listLeft) {
            //暂存未选中状态
            org.setStatus("0");
            list.add(org);
        }
        Collections.sort(list);
        List<TreeDTO> treeDTOList = new ArrayList<>();
        for (AuthOrg org : list) {
            boolean checked = "1".equals(org.getStatus());
            treeDTOList.add(new TreeDTO(org.getOrgId(), org.getParentId(),
                    String.valueOf(org.getOrgId()), org.getOrgCode(),
                    org.getOrgName(), org.getOrgName(),
                    org.getOrgSort(), "", "", org.getStatus(), checked, org.getOrgType()));
        }
        TreeDTO root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                -1, "", "", "", false, "");
        root.findChild(root, treeDTOList);
        RoleOrgDTO roleOrgDTO = new RoleOrgDTO();
        roleOrgDTO.setUnitList(unitList);
        roleOrgDTO.setOrg(root);
        return WrapperDTO.success(roleOrgDTO);
    }

    @Override
    public WrapperDTO<String> saveRoleOrg(Long roleId, String saveType, String keys) {
        Optional<AuthRole> opl = roleRepository.findById(roleId);
        if (opl.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthRole");
        }
        AuthRole role = opl.get();
        List<AuthOrg> orgList = new ArrayList<>();
        AuthOrg org;
        switch (saveType) {
            case "1":
                //配置管辖部门
                if (!StringUtils.isEmpty(keys)) {
                    String[] tmp = keys.split(",");
                    List<Long> ids = new ArrayList<>();
                    for (String s : tmp) {
                        ids.add(Long.valueOf(s));
                    }
                    orgList = orgRepository.findByOrgIdIn(ids);
                }
                List<AuthOrg> cOrgList = role.getOrgList();
                String unitCode = role.getUnitCode();
                if (cOrgList != null) {
                    cOrgList.removeIf(o -> unitCode.equals(o.getUnitCode()));
                    orgList.addAll(cOrgList);
                }
                role.setOrgList(orgList);
                break;
            case "2":
                //配置管辖单位
                org = orgRepository.findByUnitCode(keys);
                if (org == null) {
                    return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "org");
                }
                if (org.getUnitCode().equals(role.getUnitCode())) {
                    return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, "不必配置本单位为管辖单位");
                }
                orgList = role.getOrgList();
                if (orgList != null) {
                    orgList.remove(org);
                } else {
                    orgList = new ArrayList<>();
                }
                orgList.add(org);
                role.setOrgList(orgList);
                break;
            case "3":
                //删除管辖单位
                Optional<AuthOrg> oplOrg = orgRepository.findById(Long.valueOf(keys));
                if (oplOrg.isEmpty()) {
                    return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "org");
                }
                org = oplOrg.get();
                orgList = role.getOrgList();
                if (orgList != null) {
                    orgList.remove(org);
                } else {
                    orgList = new ArrayList<>();
                }
                role.setOrgList(orgList);
                break;
            default:
                break;
        }
        roleRepository.save(role);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> saveRoleMenuVols(String roleIds, String menuIds) {
        String[] tmp1 = roleIds.split(",");
        List<Long> listRoleId = new ArrayList<>();
        for (String s : tmp1) {
            listRoleId.add(Long.valueOf(s));
        }
        List<AuthRole> roles = roleRepository.findByRoleIdIn(listRoleId);
        String[] tmp = menuIds.split(",");
        List<Long> listMenuId = new ArrayList<>();
        for (String s : tmp) {
            listMenuId.add(Long.valueOf(s));
        }
        List<AuthMenu> menus = menuRepository.findByMenuIdIn(listMenuId);
        for (AuthRole role : roles) {
            List<AuthMenu> cMenus = role.getMenuList();
            cMenus.removeAll(menus);
            cMenus.addAll(menus);
        }
        roleRepository.saveAll(roles);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> save(AuthRole... roles) {
        List<AuthRole> list = Arrays.asList(roles);
        for (AuthRole role : list) {
            Optional<AuthOrg> op = orgRepository.findById(role.getOrgId());
            if (!op.isPresent()) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthOrg");
            }
            if (!authService.checkDevOps() &&
                    (!authService.checkUnitCode(op.get().getUnitCode()))) {
                //非开发者禁止保存非本单位数据
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
            }
            role.setAreaCode(op.get().getAreaCode());
            role.setUnitCode(op.get().getUnitCode());
            role.setOrg(op.get());
            if (role.getRoleId() != null && role.getRoleId() > 0) {
                Optional<AuthRole> opRole = roleRepository.findById(role.getRoleId());
                //更新时，权限不更改
                if (opRole.isPresent()) {
                    role.setMenuList(opRole.get().getMenuList());
                    role.setOrgList(opRole.get().getOrgList());
                    role.setUserList(opRole.get().getUserList());
                }
            }
        }
        roleRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(AuthRole... roles) {
        roleRepository.deleteAllInBatch(Arrays.asList(roles));
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<RoleDTO> findOne(Long id) {
        return null;
    }

    @Override
    public WrapperDTO<PageDTO<RoleDTO>> findPage(int pageSize, int pageNo, AuthRole role) {
        AuthOrg org = null;
        if (role.getOrgId() != null) {
            Optional<AuthOrg> op = orgRepository.findById(role.getOrgId());
            if (op.isPresent()) org = op.get();
        } else if (role.getUnitCode() != null) {
            org = orgRepository.findByUnitCode(role.getUnitCode());
        }
        if (org == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "org");
        }
        String name = role.getRoleName() == null ? "" : role.getRoleName();
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<RoleDTO> page = roleRepository.page(
                org.getOrgPath(), name, pageable);
        return WrapperDTO.success(new PageDTO<RoleDTO>().convert(page));
    }

}
