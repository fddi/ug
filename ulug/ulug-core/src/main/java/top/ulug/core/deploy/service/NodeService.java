package top.ulug.core.deploy.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployNode;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2020/2/11.
 * 逝者如斯夫 不舍昼夜
 */
public interface NodeService extends CurdService<DeployNode, DeployNode> {

    /**
     * 状态检测
     *
     * @return 检测结果
     */
    WrapperDTO<String> status();

    /**
     * 启动节点
     *
     * @param nodeId 节点ID
     * @return result
     */
    WrapperDTO<String> run(Long nodeId);

    /**
     * 停止节点
     *
     * @param nodeId 节点ID
     * @return result
     */
    WrapperDTO<String> stop(Long nodeId);

    /**
     * 部署包上传
     *
     * @param nodeId id
     * @param file   部署包
     * @return res
     */
    WrapperDTO<String> publish(Long nodeId, MultipartFile file);
}
