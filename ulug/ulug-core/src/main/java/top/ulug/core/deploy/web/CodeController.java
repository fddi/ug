package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.CodeCatalog;
import top.ulug.core.deploy.domain.CodeDict;
import top.ulug.core.deploy.service.CatalogService;
import top.ulug.core.deploy.service.DictService;

import java.util.List;

/**
 * Created by liujf on 2019/10/7.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/catalog-tree", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码目录树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> data(@RequestParam(value = "catalogCode", required = false) String catalogCode) throws Exception {
        return catalogService.findChildrenCatalog(catalogCode);
    }

    @RequestMapping(value = "/dev/catalog-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码目录保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute CodeCatalog catalog) throws Exception {
        return catalogService.save(catalog);
    }

    @RequestMapping(value = "/dev/catalog-del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码目录删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute CodeCatalog catalog) throws Exception {
        return catalogService.del(catalog);
    }

    @RequestMapping(value = "/catalog", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码目录详细", paramsExample = "", resultExample = "")
    public WrapperDTO<CodeCatalog> catalog(@RequestParam(value = "catalogId") Long catalogId) throws Exception {
        return catalogService.findOne(catalogId);
    }

    @RequestMapping(value = "/dict-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码字典数据列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<LabelDTO>> dictList(@RequestParam(value = "catalog") String catalog,
                                               @RequestParam(value = "dictCode", required = false) String dictCode) throws Exception {
        return dictService.listCatalogDict(catalog, dictCode);
    }

    @RequestMapping(value = "/dict-tree", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码字典树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> dictTree(@RequestParam(value = "catalog") String catalog,
                                        @RequestParam(value = "dictCode", required = false) String dictCode,
                                        @RequestParam(value = "dataType", required = false) String dataType) throws Exception {
        return dictService.findCatalogDict(catalog, dictCode, dataType);
    }

    @RequestMapping(value = "/dev/dict-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码字典保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveDict(@ModelAttribute CodeDict dict) throws Exception {
        return dictService.save(dict);
    }

    @RequestMapping(value = "/dev/file/upload", method = RequestMethod.POST)
    @ApiDocument(note = "编码字典导入", paramsExample = "", resultExample = "")
    public WrapperDTO<String> impDict(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "dataType", required = false) String dataType) throws Exception {
        return dictService.impByExcel(file, dataType);
    }

    @RequestMapping(value = "/dev/dict-del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码字典删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> delDict(@ModelAttribute CodeDict dict) throws Exception {
        return dictService.del(dict);
    }

    @RequestMapping(value = "/dict", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "编码字典详细", paramsExample = "", resultExample = "")
    public WrapperDTO<CodeDict> dict(@RequestParam(value = "dictId") Long dictId) throws Exception {
        return dictService.findOne(dictId);
    }

}
