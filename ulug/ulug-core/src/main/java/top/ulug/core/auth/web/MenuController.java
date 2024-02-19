package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.domain.AuthMenu;
import top.ulug.core.auth.dto.MenuDTO;
import top.ulug.core.auth.service.MenuService;

import java.util.List;

/**
 * Created by liujf on 2019/9/9.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/personal", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "客户端菜单树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> menuPersonal() throws Exception {
        return menuService.findPersonalMenu();
    }

    @RequestMapping(value = "/vols", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "权限菜单树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> menuVols(@RequestParam(value = "clientName") String clientName) throws Exception {
        return menuService.findMenuVols(clientName);
    }

    @RequestMapping(value = "/dev/tree", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> menuChildren(@RequestParam(value = "clientName") String clientName,
                                            @RequestParam(value = "parentId") Long parentId) throws Exception {
        return menuService.findChildrenMenu(clientName, parentId);
    }

    @RequestMapping(value = "/dev/one", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单详细", paramsExample = "", resultExample = "")
    public WrapperDTO<MenuDTO> menu(@RequestParam(value = "menuId") Long menuId) throws Exception {
        return menuService.findOne(menuId);
    }

    @RequestMapping(value = "/dev/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> menuSave(@ModelAttribute AuthMenu menu) throws Exception {
        return menuService.save(menu);
    }

    @RequestMapping(value = "/dev/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> menuDel(@ModelAttribute AuthMenu menu) throws Exception {
        return menuService.del(menu);
    }

    @RequestMapping(value = "/dev/ability-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单功能绑定列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<LabelDTO>> listAbility(@RequestParam(value = "menuId") Long menuId) throws Exception {
        return menuService.listMenuAbility(menuId);
    }

    @RequestMapping(value = "/dev/ability-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单功能绑定保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveAbility(@RequestParam(value = "menuId") Long menuId,
                                          @RequestParam(value = "keys", required = false) String keys) throws Exception {
        return menuService.saveAbility(menuId, keys);
    }

    @RequestMapping(value = "/dev/drag-drop", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "菜单层级变动", paramsExample = "", resultExample = "")
    public WrapperDTO<String> dragDrop(@RequestParam(value = "dragId") Long dragId,
                                       @RequestParam(value = "dropId") Long dropId) throws Exception {
        return menuService.dragDrop(dragId, dropId);
    }
}
