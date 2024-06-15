package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployNotice;
import top.ulug.core.deploy.domain.MessageRecord;
import top.ulug.core.deploy.domain.MultiMessage;
import top.ulug.core.deploy.service.MessageRecordService;
import top.ulug.core.deploy.service.MultiMessageService;
import top.ulug.core.deploy.service.NoticeService;

/**
 * Created by liujf on 2022/7/27.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/multiMessage")
public class MultiMessageController {
    @Autowired
    MultiMessageService multiMessageService;
    @Autowired
    MessageRecordService messageRecordService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "多元消息分页列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<MultiMessage>> page(@RequestParam(value = "pageNo") Integer pageNo,
                                                  @RequestParam(value = "pageSize") Integer pageSize,
                                                  @ModelAttribute MultiMessage multiMessage) throws Exception {
        return multiMessageService.findPage(pageSize, pageNo, multiMessage);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "多元消息保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute MultiMessage multiMessage) throws Exception {
        return multiMessageService.save(multiMessage);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "多元消息删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute MultiMessage multiMessage) throws Exception {
        return multiMessageService.del(multiMessage);
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "发送消息", paramsExample = "", resultExample = "")
    public WrapperDTO<String> send(@RequestParam(value = "selectKeys") String selectKeys, @ModelAttribute MultiMessage multiMessage) throws Exception {
        return multiMessageService.send(multiMessage, selectKeys);
    }

    @RequestMapping(value = "/page-list-record", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "消息发送记录分页列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<MessageRecord>> pageSendRecord(@RequestParam(value = "pageNo") Integer pageNo,
                                                             @RequestParam(value = "pageSize") Integer pageSize,
                                                             @ModelAttribute MessageRecord messageRecord) throws Exception {
        return messageRecordService.findPage(pageSize, pageNo, messageRecord);
    }
}
