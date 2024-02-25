package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployNotice;
import top.ulug.core.deploy.service.NoticeService;
import top.ulug.base.dto.PageDTO;

/**
 * Created by liujf on 2022/7/27.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "通知分页列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<DeployNotice>> page(@RequestParam(value = "pageNo") Integer pageNo,
                                                  @RequestParam(value = "pageSize") Integer pageSize,
                                                  @ModelAttribute DeployNotice notice) throws Exception {
        return noticeService.findPage(pageSize, pageNo, notice);
    }

    @RequestMapping(value = "/public-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "通知分页列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<DeployNotice>> publicList(@RequestParam(value = "pageNo") Integer pageNo,
                                                        @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        return noticeService.pagePublic(pageSize, pageNo);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "通知保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute DeployNotice notice) throws Exception {
        return noticeService.save(notice);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "客户端删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute DeployNotice notice) throws Exception {
        return noticeService.del(notice);
    }

}
