package top.ulug.core.auth.service;

import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.auth.domain.AuthMenu;
import top.ulug.core.auth.dto.MenuDTO;
import top.ulug.jpa.CurdService;

import java.util.List;

/**
 * Created by liujf on 2019/4/1.
 * 逝者如斯夫 不舍昼夜
 */
public interface MenuService extends CurdService<AuthMenu, MenuDTO> {

    /**
     * 通过clientName匹配客户端菜单，返回树结构的子菜单数据，parentId为0时：查询全部菜单
     * parenId>0时，返回父id所有子菜单
     *
     * @param clientName 客户端名称
     * @param parentKey  父ID
     * @return dto
     */
    WrapperDTO<TreeDTO> findChildrenMenu(String clientName, Long parentKey);

    /**
     * 通过clientName匹配客户端菜单，筛选返回用于配置用户权限的树结构菜单
     * 基于当前用户权限返回
     *
     * @param clientName 客户端名称
     * @return dto
     */
    WrapperDTO<TreeDTO> findMenuVols(String clientName);


    /**
     * 通过clientName匹配客户端菜单，筛选返回用户有访问权限的树结构菜单
     *
     * @return dto
     */
    WrapperDTO<TreeDTO> findPersonalMenu();

    /**
     * 通过clientName匹配客户端菜单，筛选返回用户有访问权限的菜单集合
     *
     * @param clientName 客户端名称
     * @return dto
     */
    List<AuthMenu> listPersonalMenu(String clientName);


    /**
     * 返回菜单绑定接口列表，使用checked标识菜单是否绑定接口权限
     *
     * @param menuId 菜单id
     * @return dto
     */
    WrapperDTO<List<LabelDTO>> listMenuAbility(Long menuId);

    /**
     * 保存菜单所有绑定接口列表
     *
     * @param menuId 角色ID
     * @param keys   接口ID集
     * @return dto
     */
    WrapperDTO<String> saveAbility(Long menuId, String keys);

    /**
     * 菜单层级变动
     *
     * @param dragId 菜单Id
     * @param dropId 目标Id
     * @return dto
     */
    WrapperDTO<String> dragDrop(Long dragId, Long dropId);
}
