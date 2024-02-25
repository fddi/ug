package top.ulug.core.deploy.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

/**
 * Created by liujf on 2020/2/11.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class DeployNode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long nodeId;

    @Column(unique = true, nullable = false)
    private String serverName;

    @Column(nullable = false)
    private String nodeAddress;

    @Column(nullable = false, length = 50)
    private String nodePort;

    @Column(nullable = false)
    private String nodePath;

    @Column(length = 50)
    private String nodeType;

    @Column(nullable = false, length = 50)
    private String nodeStatus;

    private String nodeNote;

    private String publishFile;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public String getNodePort() {
        return nodePort;
    }

    public void setNodePort(String nodePort) {
        this.nodePort = nodePort;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getNodeNote() {
        return nodeNote;
    }

    public void setNodeNote(String nodeNote) {
        this.nodeNote = nodeNote;
    }

    public String getPublishFile() {
        return publishFile;
    }

    public void setPublishFile(String publishFile) {
        this.publishFile = publishFile;
    }
}
