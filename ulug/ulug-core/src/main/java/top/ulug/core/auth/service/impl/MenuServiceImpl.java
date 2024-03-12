package top.ulug.core.auth.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.e.SQLikeEnum;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthMenu;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.dto.MenuDTO;
import top.ulug.core.auth.repository.AuthMenuRepository;
import top.ulug.core.auth.repository.AuthRoleRepository;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.auth.service.MenuService;
import top.ulug.core.deploy.domain.DeployAbility;
import top.ulug.core.deploy.repository.DeployAbilityRepository;
import top.ulug.core.deploy.service.CacheService;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujf on 2019/4/1.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Value("${project.auth.active.time}")
    private Long activeTime;
    @Autowired
    private AuthRoleRepository roleRepository;
    @Autowired
    private AuthMenuRepository menuRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private DeployAbilityRepository abilityRepository;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CacheService cacheService;

    @Override
    public WrapperDTO<TreeDTO> findChildrenMenu(String clientName, Long parentId) {
        TreeDTO root;
        if (0 == parentId) {
            root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                    -1, "", "", "", false, "");
        } else {
            AuthMenu menu = menuRepository.findById(parentId).get();
            root = new TreeDTO(menu.getMenuId(), menu.getParentId(), String.valueOf(menu.getMenuId()),
                    menu.getMenuUri(), menu.getMenuName(), menu.getMenuName(),
                    menu.getMenuSort(), menu.getIcon(), menu.getMenuNote(), menu.getStatus(), false, menu.getMenuType());
        }
        List<AuthMenu> list = menuRepository
                .findByClientNameOrderByParentId(clientName);
        Collections.sort(list);
        List<TreeDTO> treeDTOList = new ArrayList<>();
        for (AuthMenu menu : list) {
            treeDTOList.add(new TreeDTO(menu.getMenuId(), menu.getParentId(), String.valueOf(menu.getMenuId()),
                    menu.getMenuUri(), menu.getMenuName(), menu.getMenuName(),
                    menu.getMenuSort(), menu.getIcon(), menu.getMenuNote(), menu.getStatus(), false, menu.getMenuType()));
        }
        root.findChild(root, treeDTOList);
        return WrapperDTO.success(root);
    }

    @Override
    public WrapperDTO<TreeDTO> findMenuVols(String clientName) {
        TreeDTO root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                -1, "", "", "", false, "");
        List<TreeDTO> treeDTOList = new ArrayList<>();
        List<AuthMenu> personalMenus = this.listPersonalMenu(clientName);
        if (personalMenus == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        for (AuthMenu menu : personalMenus) {
            //过滤开发者菜单
            if (menu.getMenuUri() != null && menu.getMenuUri().contains("/dev-")) {
                continue;
            }
            treeDTOList.add(new TreeDTO(menu.getMenuId(), menu.getParentId(), String.valueOf(menu.getMenuId()),
                    menu.getMenuUri(), menu.getMenuName(), menu.getMenuName(),
                    menu.getMenuSort(), menu.getIcon(), menu.getMenuNote(), menu.getStatus(), false, menu.getMenuType()));
        }
        root.findChild(root, treeDTOList);
        return WrapperDTO.success(root);
    }

    @Override
    public WrapperDTO<TreeDTO> findPersonalMenu() {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO.getMenu() != null) {
            //缓存数据
            return WrapperDTO.success(authDTO.getMenu());
        }
        TreeDTO root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                -1, "", "", "", false, "");
        List<TreeDTO> treeDTOList = new ArrayList<>();
        List<AuthMenu> personalMenus = this.listPersonalMenu(appId);
        if (personalMenus == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        for (AuthMenu menu : personalMenus) {
            treeDTOList.add(new TreeDTO(menu.getMenuId(), menu.getParentId(), String.valueOf(menu.getMenuId()),
                    menu.getMenuUri(), menu.getMenuName(), menu.getMenuName(),
                    menu.getMenuSort(), menu.getIcon(), menu.getMenuNote(), menu.getStatus(), false, menu.getMenuType()));
        }
        root.findChild(root, treeDTOList);
        authDTO.setMenu(root);
        cacheService.cacheAuth(appId, token, authDTO);
        return WrapperDTO.success(root);
    }

    @Override
    public List<AuthMenu> listPersonalMenu(String clientName) {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return null;
        }
        List<AuthMenu> personalMenus = new ArrayList<>();
        if (authService.checkDevOps()) {
            personalMenus = menuRepository.findAll();
        } else {
            List<AuthRole> inRoles = roleRepository.findAllByUserList_UserName(
                    authDTO.getAccount().getUserName());
            for (AuthRole role : inRoles) {
                List<AuthMenu> menus = role.getMenuList();
                personalMenus.removeAll(menus);
                personalMenus.addAll(menus);
            }
        }
        //通过clientName筛选数据
        personalMenus = this.filterMenuByClientName(personalMenus, clientName);
        List<AuthMenu> pMenus = new ArrayList<>();
        for (AuthMenu menu : personalMenus) {
            if (menu.getParentId() == 0) {
                continue;
            }
            /*若子业务菜单有权限，显示其父通用菜单*/
            /*查找是否已存在*/
            boolean has = false;
            for (AuthMenu pMenu : personalMenus) {
                if (pMenu.getMenuId().equals(menu.getParentId())) {
                    has = true;
                    break;
                }
            }
            /*查找是否已添加*/
            if (!has) {
                pMenus.add(menuRepository.findById(menu.getParentId()).get());
                Optional<AuthMenu> opt = menuRepository.findById(menu.getParentId());
                if (opt.isPresent()) {
                    pMenus.add(opt.get());
                    String pId = opt.get().getMenuPath().split(">")[0];
                    pMenus.add(menuRepository.findById(Long.valueOf(pId)).get());
                }
            }
        }
        //去除重复数据,及无效数据
        pMenus = this.deduplication(pMenus);
        personalMenus.addAll(pMenus);
        Collections.sort(personalMenus);
        return personalMenus;
    }

    @Override
    public WrapperDTO<List<LabelDTO>> listMenuAbility(Long menuId) {
        //已选中的接口列表
        AuthMenu menu = menuRepository.findById(menuId).get();
        List<DeployAbility> listRight = menu.getAbilityList();
        List<LabelDTO> list = new ArrayList<>();
        for (DeployAbility ability : listRight) {
            LabelDTO labelDTO = new LabelDTO();
            labelDTO.setKey(String.valueOf(ability.getAbilityId()));
            labelDTO.setTitle(ability.getAbilityNote() + "(" +
                    ability.getAbilityUri() + ")");
            labelDTO.setChecked(true);
            list.add(labelDTO);
        }
        //待选择接口列表
        List<DeployAbility> listLeft = authService.listUserAbility();
        listLeft.removeAll(listRight);
        for (DeployAbility ability : listLeft) {
            LabelDTO labelDTO = new LabelDTO();
            labelDTO.setKey(String.valueOf(ability.getAbilityId()));
            labelDTO.setTitle(ability.getAbilityNote() + "(" +
                    ability.getAbilityUri() + ")");
            labelDTO.setChecked(false);
            list.add(labelDTO);
        }
        return WrapperDTO.success(list);
    }

    @Override
    public WrapperDTO<String> saveAbility(Long menuId, String keys) {
        List<DeployAbility> abilities = null;
        if (!StringUtils.isEmpty(keys)) {
            String[] tmp = keys.split(",");
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < tmp.length; i++) {
                ids.add(Long.parseLong(tmp[i]));
            }
            abilities = abilityRepository.findByAbilityIdIn(ids);
        }
        AuthMenu menu = menuRepository.findById(menuId).get();
        menu.getAbilityList().clear();
        menu.setAbilityList(abilities);
        menuRepository.save(menu);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> dragDrop(Long dragId, Long dropId) {
        Optional<AuthMenu> optDrag = menuRepository.findById(dragId);
        if (!optDrag.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthMenu");
        }
        AuthMenu menu = optDrag.get();
        String path = "";
        if (dropId > 0) {
            path = menuRepository.findPath(dropId) + ">";
        }
        path += menu.getMenuId();
        menu.setParentId(dropId);
        menu.setMenuPath(path);
        menuRepository.save(menu);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> save(AuthMenu... menus) {
        List<AuthMenu> list = Arrays.asList(menus);
        for (AuthMenu menu : list) {
            if (menu.getMenuId() != null && menu.getMenuId() > 0) {
                AuthMenu old = menuRepository.findById(menu.getMenuId()).get();
                //更新字段时绑定接口不更改
                List<DeployAbility> abList = old.getAbilityList();
                menu.setAbilityList(abList);
            }
        }
        //保存后获取id,用于产生路径
        List<AuthMenu> saveList = menuRepository.saveAll(list);
        for (AuthMenu menu : saveList) {
            String path = "";
            if (menu.getParentId() != null && menu.getParentId() > 0) {
                path = menuRepository.findPath(menu.getParentId()) + ">";
            }
            path += menu.getMenuId();
            menu.setMenuPath(path);
        }
        menuRepository.saveAll(saveList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(AuthMenu... menus) {
        //删除下属所有数据
        List<AuthMenu> list = Arrays.asList(menus);
        List<AuthMenu> delList = new ArrayList<>();
        for (AuthMenu menu : list) {
            String path = menuRepository.findPath(menu.getMenuId());
            List<AuthMenu> childList = menuRepository.findByMenuPath(path + ">");
            delList.addAll(childList);
        }
        delList.addAll(list);
        menuRepository.deleteAllInBatch(delList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<MenuDTO> findOne(Long id) {
        Optional<AuthMenu> optional = menuRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        return WrapperDTO.success(
                modelMapper.map(optional.get(), MenuDTO.class));
    }

    @Override
    public WrapperDTO<PageDTO<MenuDTO>> findPage(int pageSize, int pageNo, AuthMenu authMenu) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<MenuDTO> page = menuRepository.findByMenuNameLikeOrMenuUriLike(
                StringUtils.linkSQLike(authMenu.getMenuName(), SQLikeEnum.ALL),
                StringUtils.linkSQLike(authMenu.getMenuUri(), SQLikeEnum.ALL), pageable);
        return WrapperDTO.success(new PageDTO<MenuDTO>().convert(page));
    }

    private List<AuthMenu> filterMenuByClientName(List<AuthMenu> menus, String clientName) {
        if (menus == null || StringUtils.isEmpty(clientName)) {
            return menus;
        }
        List<AuthMenu> newList = new ArrayList<>();
        for (Iterator iter = menus.iterator(); iter.hasNext(); ) {
            AuthMenu menu = (AuthMenu) iter.next();
            if (clientName.equals(menu.getClientName()) && "1".equals(menu.getStatus())) {
                newList.add(menu);
            }
        }
        return newList;
    }

    private List<AuthMenu> deduplication(List<AuthMenu> list) {
        List<AuthMenu> newList = new ArrayList<>();
        Set set = new HashSet();
        for (AuthMenu menu : list) {
            if (set.add(menu.getMenuId()) && "1".equals(menu.getStatus())) {
                newList.add(menu);
            }
        }
        return newList;
    }
}
