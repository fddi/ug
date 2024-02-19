package top.ulug.cms.media.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.cms.media.domain.MediaContent;
import top.ulug.cms.media.dto.ContentDTO;
import top.ulug.cms.media.service.ContentService;
import top.ulug.jpa.dto.PageDTO;

/**
 * Created by liujf on 2022/7/17.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    ContentService contentService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "内容列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<ContentDTO>> findPage(
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize,
            @ModelAttribute MediaContent content) {
        return contentService.findPage(pageSize, pageNo, content);
    }

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ApiDocument(note = "内容保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute MediaContent content,
                                   @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        return contentService.upload(content, image);
    }
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "内容删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute MediaContent content) throws Exception {

        return contentService.del(content);
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "内容发布", paramsExample = "", resultExample = "")
    public WrapperDTO<String> publish(@RequestParam(value = "contentId") Long contentId,
                                      @RequestParam(value = "status") String status) throws Exception {
        return contentService.updatePublishStatus( contentId, status);
    }

    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "内容排序", paramsExample = "", resultExample = "")
    public WrapperDTO<String> sort(@RequestParam(value = "contentId") Long contentId,
                                   @RequestParam(value = "sort") int sort) throws Exception {
        return contentService.updateSort(contentId, sort);
    }

    @RequestMapping(value = "/one", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "内容详细", paramsExample = "", resultExample = "")
    public WrapperDTO<ContentDTO> one(@RequestParam(value = "contentId") Long contentId) throws Exception {
        return contentService.findOne(contentId);
    }
}
