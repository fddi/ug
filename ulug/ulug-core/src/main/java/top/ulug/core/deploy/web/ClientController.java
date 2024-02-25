package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployClient;
import top.ulug.core.deploy.service.ClientService;
import top.ulug.base.dto.PageDTO;

import java.util.List;

/**
 * Created by liujf on 2019/9/15.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/dev/page", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "客户端分页列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<DeployClient>> page(@RequestParam(value = "pageNo") Integer pageNo,
                                                  @RequestParam(value = "pageSize") Integer pageSize,
                                                  @ModelAttribute DeployClient client) throws Exception {
        return clientService.findPage(pageSize, pageNo, client);
    }


    @RequestMapping(value = "/dev/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "客户端保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute DeployClient client) throws Exception {
        return clientService.save(client);
    }

    @RequestMapping(value = "/dev/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "客户端删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute DeployClient client) throws Exception {
        return clientService.del(client);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "客户端列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<LabelDTO>> list() throws Exception {
        return clientService.clientList();
    }
}
