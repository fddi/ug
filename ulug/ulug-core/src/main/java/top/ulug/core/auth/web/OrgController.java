package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.domain.AuthOrg;
import top.ulug.core.auth.service.OrgService;
import top.ulug.jpa.dto.PageDTO;

import java.util.List;

/**
 * Created by liujf on 2019/9/9.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @RequestMapping(value = "/children", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "机构树数据", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> orgChildren(@RequestParam(value = "areaCode", required = false) String areaCode,
                                           @RequestParam(value = "parentId", required = false) Long parentId) throws Exception {
        return orgService.findChildrenOrg(areaCode, parentId);
    }

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "机构列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<AuthOrg>> pageList(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @ModelAttribute AuthOrg org) {
        return orgService.findPage(pageSize, pageNo, org);
    }

    @RequestMapping(value = "/one", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "机构详细", paramsExample = "", resultExample = "")
    public WrapperDTO<AuthOrg> org(@RequestParam(value = "orgId") Long orgId) throws Exception {
        return orgService.findOne(orgId);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "机构保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> orgSave(@ModelAttribute AuthOrg org) throws Exception {
        return orgService.save(org);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "机构删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> orgDel(@ModelAttribute AuthOrg org) throws Exception {
        return orgService.del(org);
    }

    @RequestMapping(value = "/dev/file/upload", method = RequestMethod.POST)
    @ApiDocument(note = "机构数据导入", paramsExample = "", resultExample = "")
    public WrapperDTO<String> impDict(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "dataType", required = false) String dataType) throws Exception {
        return orgService.impByExcel(file, dataType);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiDocument(note = "机构查找", paramsExample = "", resultExample = "")
    public WrapperDTO<List<LabelDTO>> search(@RequestParam(value = "searchKey", required = false) String searchKey,
                                             @RequestParam(value = "orgType", required = false) String orgType) throws Exception {
        return orgService.search(searchKey, orgType);
    }

    @RequestMapping(value = "/drag-drop", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "机构层级变动", paramsExample = "", resultExample = "")
    public WrapperDTO<String> dragDrop(@RequestParam(value = "dragId") Long dragId,
                                       @RequestParam(value = "dropId") Long dropId) throws Exception {
        return orgService.dragDrop(dragId, dropId);
    }
}
