package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.dto.RoleDTO;
import top.ulug.core.auth.dto.RoleOrgDTO;
import top.ulug.core.auth.service.RoleService;
import top.ulug.jpa.dto.PageDTO;

import java.util.List;

/**
 * Created by liujf on 2019/10/31.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<RoleDTO>> roleList(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 @ModelAttribute AuthRole role) throws Exception {
        return roleService.findPage(pageSize, pageNo, role);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> roleSave(@ModelAttribute AuthRole role) throws Exception {
        return roleService.save(role);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> roleDel(@ModelAttribute AuthRole role) throws Exception {
        return roleService.del(role);
    }

    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位复制", paramsExample = "", resultExample = "")
    public WrapperDTO<String> roleCopy(@RequestParam(value = "orgId") Long orgId,
                                       @RequestParam(value = "selectKeys") String selectKeys) throws Exception {
        return roleService.roleCopy(orgId, selectKeys);
    }

    @RequestMapping(value = "/menu-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位菜单绑定列表", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> roleMenu(@RequestParam(value = "roleId") Long roleId,
                                        @RequestParam(value = "clientName") String clientName) throws Exception {
        return roleService.listRoleMenu(roleId, clientName);
    }

    @RequestMapping(value = "/menu-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位菜单绑定保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveRoleMenu(@RequestParam(value = "roleId") Long roleId,
                                           @RequestParam(value = "clientName") String clientName,
                                           @RequestParam(value = "keys", required = false) String keys) throws Exception {
        return roleService.saveRoleMenu(roleId, clientName, keys);
    }

    @RequestMapping(value = "/menu-vols-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位菜单批量设置", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveRoleMenuVols(@RequestParam(value = "roleIds") String roleIds,
                                               @RequestParam(value = "menuIds") String menuIds) throws Exception {
        return roleService.saveRoleMenuVols(roleIds, menuIds);
    }

    @RequestMapping(value = "/org-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位机构绑定列表", paramsExample = "", resultExample = "")
    public WrapperDTO<RoleOrgDTO> roleOrg(@RequestParam(value = "roleId") Long roleId) throws Exception {
        return roleService.listRoleOrg(roleId);
    }

    @RequestMapping(value = "/org-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位机构绑定保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveRoleOrg(@RequestParam(value = "roleId") Long roleId,
                                          @RequestParam(value = "saveType", defaultValue = "1") String saveType,
                                          @RequestParam(value = "keys", required = false) String keys) throws Exception {
        return roleService.saveRoleOrg(roleId, saveType, keys);
    }

    @RequestMapping(value = "/user-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位用户绑定列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<LabelDTO>> roleUser(@RequestParam(value = "unitCode", required = false) String unitCode,
                                               @RequestParam(value = "roleId") Long roleId) throws Exception {
        return roleService.listRoleUser(unitCode, roleId);
    }

    @RequestMapping(value = "/user-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "岗位用户绑定保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveRoleUser(@RequestParam(value = "roleId") Long roleId,
                                           @RequestParam(value = "keys", required = false) String keys) throws Exception {
        return roleService.saveRoleUser(roleId, keys);
    }

}
