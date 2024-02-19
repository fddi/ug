package top.ulug.cms.media.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.SubjectDTO;
import top.ulug.cms.media.service.SubjectService;
import top.ulug.jpa.dto.PageDTO;

/**
 * Created by liujf on 2022/7/14.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "媒体主题列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<SubjectDTO>> findPage(
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize,
            @ModelAttribute MediaSubject subject) {
        return subjectService.findPage(pageSize, pageNo, subject);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "媒体主题保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute MediaSubject subject) throws Exception {
        return subjectService.save(subject);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "媒体主题保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute MediaSubject subject) throws Exception {
        return subjectService.del(subject);
    }

    @RequestMapping(value = "/file/upload-logo", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "业务号图标上传", paramsExample = "", resultExample = "")
    public WrapperDTO<String> uploadLogo(@RequestParam(value = "subjectId") Long subjectId,
                                         @RequestParam("logo") MultipartFile logo) {
        return subjectService.uploadLogo(subjectId, logo);
    }
}
