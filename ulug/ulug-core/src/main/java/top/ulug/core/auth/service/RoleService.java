package top.ulug.core.auth.service;


import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.dto.RoleDTO;
import top.ulug.core.auth.dto.RoleOrgDTO;
import top.ulug.base.inf.CurdService;

import java.util.List;

/**
 * Created by liujf on 2019/4/2.
 * 逝者如斯夫 不舍昼夜
 */
public interface RoleService extends CurdService<AuthRole, RoleDTO> {

    /**
     * @param orgId   机构ID
     * @param roleIds 岗位id集合
     * @return result
     */
    WrapperDTO<String> roleCopy(Long orgId, String roleIds);

    /**
     * 返回角色用户列表，orgId下所有用户集合,同时返回checked标识的角色id下所有用户
     *
     * @param unitCode 机构代码
     * @param roleId   角色ID
     * @return dto
     */
    WrapperDTO<List<LabelDTO>> listRoleUser(String unitCode, Long roleId);

    /**
     * 保存角色id下所有用户
     *
     * @param roleId 角色ID
     * @param keys   用户ID集
     * @return dto
     */
    WrapperDTO<String> saveRoleUser(Long roleId, String keys);

    /**
     * 返回角色可配置菜单列表，使用checked标识角色是否拥有菜单权限
     *
     * @param roleId     角色ID
     * @param clientName 客户端名称
     * @return dto
     */
    WrapperDTO<TreeDTO> listRoleMenu(Long roleId, String clientName);

    /**
     * 保存角色id下所有菜单列表
     *
     * @param roleId     角色ID
     * @param clientName 客户端名称
     * @param keys       菜单ID集
     * @return dto
     */
    WrapperDTO<String> saveRoleMenu(Long roleId, String clientName, String keys);

    /**
     * 返回角色可配置部门列表，使用checked标识角色是否拥有菜单权限
     *
     * @param roleId 角色ID
     * @return dto
     */
    WrapperDTO<RoleOrgDTO> listRoleOrg(Long roleId);

    /**
     * 保存角色id下所有部门列表
     *
     * @param roleId   角色ID
     * @param saveType 保存类型 1-本机构更改 2-管辖机构增加 3-管辖机构剔除
     * @param keys     部门ID集
     * @return dto
     */
    WrapperDTO<String> saveRoleOrg(Long roleId, String saveType, String keys);

    /**
     * 批量保存菜单权限
     *
     * @param roleIds 角色集
     * @param menuIds 菜单集
     * @return dto
     */
    WrapperDTO<String> saveRoleMenuVols(String roleIds, String menuIds);
}
