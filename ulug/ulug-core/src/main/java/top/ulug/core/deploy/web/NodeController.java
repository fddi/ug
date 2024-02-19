package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployNode;
import top.ulug.core.deploy.service.NodeService;
import top.ulug.jpa.dto.PageDTO;

/**
 * Created by liujf on 2019/9/15.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/node")
public class NodeController {
    @Autowired
    private NodeService nodeService;

    @RequestMapping(value = "/page-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "节点服务维护列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<DeployNode>> page(@RequestParam(value = "pageNo") Integer pageNo,
                                                @RequestParam(value = "pageSize") Integer pageSize) throws Exception {
        return nodeService.findPage(pageSize, pageNo, null);
    }

    @RequestMapping(value = "/dev/save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "节点服务保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute DeployNode node) throws Exception {
        return nodeService.save(node);
    }

    @RequestMapping(value = "/dev/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "节点服务删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute DeployNode node) throws Exception {
        return nodeService.del(node);
    }

    @RequestMapping(value = "/dev/start", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "节点服务启动", paramsExample = "", resultExample = "")
    public WrapperDTO<String> start(@RequestParam(value = "nodeId") Long nodeId) throws Exception {
        return nodeService.run(nodeId);
    }

    @RequestMapping(value = "/dev/stop", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "节点服务停止", paramsExample = "", resultExample = "")
    public WrapperDTO<String> stop(@RequestParam(value = "nodeId") Long nodeId) throws Exception {
        return nodeService.stop(nodeId);
    }

    @RequestMapping(value = "/dev/status", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "节点服务状态", paramsExample = "", resultExample = "")
    public WrapperDTO<String> status() throws Exception {
        return nodeService.status();
    }

    @RequestMapping(value = "/dev/file/publish", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "服务部署上传", paramsExample = "", resultExample = "")
    public WrapperDTO<String> publish(@RequestParam(value = "nodeId") Long nodeId,
                                      @RequestParam(value = "file") MultipartFile file) throws Exception {
        return nodeService.publish(nodeId, file);
    }
}
