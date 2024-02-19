package top.ulug.core.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.auth.domain.AuthFormData;
import top.ulug.core.auth.service.FormDataService;

/**
 * Created by liujf on 2021/5/25.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/form")
public class FormDataController {

    @Autowired
    private FormDataService formDataService;

    @RequestMapping(value = "/dev/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "表单管理列表", paramsExample = "", resultExample = "")
    public String list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                       @ModelAttribute AuthFormData data) throws Exception {
        return formDataService.findPage(pageSize, pageNo, data).toString();
    }


    @RequestMapping(value = "/dev/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "表单保存", paramsExample = "", resultExample = "")
    public String save(@ModelAttribute AuthFormData data) throws Exception {
        return formDataService.save(data).toString();
    }

    @RequestMapping(value = "/dev/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "表单删除", paramsExample = "", resultExample = "")
    public String del(@ModelAttribute AuthFormData data) throws Exception {
        return formDataService.del(data).toString();
    }

    @RequestMapping(value = "/mapper", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "表单配置", paramsExample = "", resultExample = "")
    public String mapper(@RequestParam(value = "formCode") String formCode) throws Exception {
        return formDataService.mapper(formCode).toString();
    }
}

