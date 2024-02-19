package top.ulug.core.deploy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.CommandEnum;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.util.CommandExecute;
import top.ulug.base.util.FileUtils;
import top.ulug.base.util.StreamUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.deploy.domain.DeployNode;
import top.ulug.core.deploy.repository.DeployNodeRepository;
import top.ulug.core.deploy.service.NodeService;
import top.ulug.jpa.dto.PageDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020/2/11.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class NodeServiceImpl implements NodeService {
    @Autowired
    private DeployNodeRepository nodeRepository;
    @Value("${project.node.address}")
    private String nodeAddress;
    @Value("${project.node.path}")
    private String nodeMgrPath;
    @Value("${project.node.zip}")
    private String nodeZipPath;
    @Value("${project.node.dist}")
    private String nodeDist;
    @Autowired
    private AuthService authService;

    @Override
    public WrapperDTO<String> status() {
        String res = "部署路径：" + nodeMgrPath + "\r\n";
        File nodePath = new File(nodeMgrPath);
        try {
            if (CommandExecute.isOSLinux()) {
                String[] ns = {"/bin/sh", "-c", CommandEnum.COMMAND_NODE_VERSION.getLinuxCmd()};
                res += "node-js版本：" + CommandExecute.execute(ns, nodePath);
                String[] ps = {"/bin/sh", "-c", CommandEnum.COMMAND_PM2_VERSION.getLinuxCmd()};
                res += "pm2版本：" + CommandExecute.execute(ps, nodePath);
            } else {
                res += "node-js版本：" + CommandExecute.executeCommand(
                        CommandEnum.COMMAND_NODE_VERSION.getWinCmd(), nodePath);
                res += "pm2版本：" + CommandExecute.executeCommand(
                        CommandEnum.COMMAND_PM2_VERSION.getWinCmd(), nodePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, e.getMessage());
        }
        return WrapperDTO.success(res);
    }

    @Override
    public WrapperDTO<String> run(Long nodeId) {
        Optional<DeployNode> ol = nodeRepository.findById(nodeId);
        if (!ol.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "DeployNode");
        }
        DeployNode node = ol.get();
        if (!"1".equals(node.getNodeStatus())) {
            File nodePath = new File(node.getNodePath());
            try {
                if (CommandExecute.isOSLinux()) {
                    String[] ns = {"/bin/sh", "-c", CommandEnum.COMMAND_PM2_START.getLinuxCmd() + node.getServerName()};
                    CommandExecute.execute(ns, nodePath);
                } else {
                    CommandExecute.executeCommand(
                            CommandEnum.COMMAND_PM2_START.getWinCmd() + node.getServerName(), nodePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, e.getMessage());
            }
        }
        node.setNodeStatus("1");
        nodeRepository.save(node);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> stop(Long nodeId) {
        Optional<DeployNode> ol = nodeRepository.findById(nodeId);
        if (!ol.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "DeployNode");
        }
        DeployNode node = ol.get();
        if ("1".equals(node.getNodeStatus())) {
            File nodePath = new File(node.getNodePath());
            try {
                if (CommandExecute.isOSLinux()) {
                    String[] ns = {"/bin/sh", "-c", CommandEnum.COMMAND_PM2_STOP.getLinuxCmd() + node.getServerName()};
                    CommandExecute.execute(ns, nodePath);
                } else {
                    CommandExecute.executeCommand(
                            CommandEnum.COMMAND_PM2_STOP.getWinCmd() + node.getServerName(), nodePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, e.getMessage());
            }
        }
        node.setNodeStatus("0");
        nodeRepository.save(node);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> publish(Long nodeId, MultipartFile file) {
        if (!authService.checkDevOps()) {
            return WrapperDTO.noPermission();
        }
        if (file == null) {
            return WrapperDTO.npe("文件不存在");
        }
        Optional<DeployNode> optional = nodeRepository.findById(nodeId);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("节点不存在");
        }
        String filename = file.getOriginalFilename();
        assert filename != null;
        DeployNode node = optional.get();
        String zipPath = nodeMgrPath + "/" + node.getServerName();
        try {
            FileUtils.saveFile(file, zipPath, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File appZip = new File(zipPath, filename);
        if (!appZip.exists()) {
            return WrapperDTO.npe("文件上传失败");
        }
        String appPath = this.nodeMgrPath + "/" + node.getServerName() + "/" + nodeDist;
        if (!StringUtils.isEmpty(node.getPublishFile())) {
            FileUtils.delFolder(appPath);
        }
        FileUtils.createDirIfNotExist(appPath);
        try {
            FileUtils.unZip(appZip, appPath);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapperDTO.fail("文件解压失败");
        }
        node.setPublishFile(filename);
        nodeRepository.save(node);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> save(DeployNode... nodes) {
        List<DeployNode> list = Arrays.asList(nodes);
        for (DeployNode node : list) {
            node.setNodeAddress(nodeAddress);
            node.setNodeStatus("0");
            String path = this.createNodePath(node.getServerName());
            if (path == null) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "path");
            }
            node.setNodePath(path);
            this.initNodeConfig(path, node.getNodePort());
        }
        nodeRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(DeployNode... nodes) {
        List<DeployNode> list = Arrays.asList(nodes);
        for (DeployNode n : list) {
            Optional<DeployNode> ol = nodeRepository.findById(n.getNodeId());
            if (!ol.isPresent()) {
                continue;
            }
            DeployNode node = ol.get();
            if ("1".equals(node.getNodeStatus())) {
                //运行中的节点，立即停止
                WrapperDTO<String> dtoStop = this.stop(node.getNodeId());
                if (ResultMsgEnum.RESULT_SUCCESS.getCode() != dtoStop.getResultCode()) {
                    return dtoStop;
                }
            }
            //删除pm2进程
            File nodePath = new File(node.getNodePath());
            try {
                if (CommandExecute.isOSLinux()) {
                    String[] ns = {"/bin/sh", "-c", CommandEnum.COMMAND_PM2_DEL.getLinuxCmd() + node.getServerName()};
                    CommandExecute.execute(ns, nodePath);
                } else {
                    CommandExecute.executeCommand(
                            CommandEnum.COMMAND_PM2_DEL.getWinCmd() + node.getServerName(), nodePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, e.getMessage());
            }
            //删除目录文件
            this.dropNodePath(node.getServerName());
        }
        nodeRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<DeployNode> findOne(Long id) {
        Optional<DeployNode> optional = nodeRepository.findById(id);
        if (!optional.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, String.valueOf(id));
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<DeployNode>> findPage(int pageSize, int pageNo, DeployNode deployNode) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<DeployNode> page = nodeRepository.findAll(pageable);
        return WrapperDTO.success(new PageDTO<DeployNode>().convert(page));
    }

    /**
     * 在服务器指定目录创建node节点
     *
     * @return 节点路径
     */
    private String createNodePath(String nodeName) {
        //查找节点压缩包
        File dirName = new File(this.nodeMgrPath);
        if (!dirName.exists()) {
            return null;
        }
        File nodeZip = new File(this.nodeMgrPath + "/" + this.nodeZipPath);
        if (!nodeZip.exists()) {
            return null;
        }
        String nodePath = this.nodeMgrPath + "/" + nodeName;
        File nodeFile = new File(nodePath);
        if (nodeFile.exists()) {
            return nodePath;
        }
        FileUtils.createDirIfNotExist(nodePath);
        try {
            FileUtils.unZip(nodeZip, nodePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return nodePath;
    }

    /**
     * 在服务器指定目录删除node节点
     *
     * @return 节点路径
     */
    private void dropNodePath(String nodeName) {
        //查找节点目录
        if (StringUtils.isEmpty(nodeName)) {
            return;
        }
        String nodePath = this.nodeMgrPath + "/" + nodeName;
        FileUtils.delFolder(nodePath);
    }

    /**
     * 节点配置文件初始化
     *
     * @param nodePath 节点路径
     * @param port     端口
     */
    private void initNodeConfig(String nodePath, String port) {
        File config = new File(nodePath + "/config.json");
        JSONObject json = new JSONObject();
        try {
            if (config.exists()) {
                FileInputStream inputStream = new FileInputStream(config);
                String configJson = StreamUtils.readStream(inputStream);
                JSON.parseObject(configJson);
            } else {
                FileUtils.createFileIfNotExist(config);
            }
            json.put("port", port);
            FileWriter writer = new FileWriter(config);
            writer.write(json.toJSONString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
