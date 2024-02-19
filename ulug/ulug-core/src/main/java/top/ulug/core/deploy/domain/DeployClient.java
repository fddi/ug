package top.ulug.core.deploy.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 客户端管理
 * Created by liujf on 2019/3/24.
 * 逝者如斯夫 不舍昼夜
 */

@Entity
@Table
public class DeployClient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clientId;

    @Column(columnDefinition = "VARCHAR(2) COMMENT '状态 1--正常 0--异常'")
    private String status;

    @Column(unique = true, nullable = false,
            columnDefinition = "VARCHAR(50) COMMENT '客户端名称'")
    private String clientName;

    @Column(unique = true, nullable = false,
            columnDefinition = "VARCHAR(40) COMMENT '客户端密钥'")
    private String clientKey;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '客户端秘钥说明'")
    private String clientKeyNote;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '客户端类型 1-官方客户端'")
    private String clientType;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '客户端说明'")
    private String clientNote;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '图标'")
    private String icon;

    public DeployClient() {

    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNote() {
        return clientNote;
    }

    public void setClientNote(String clientNote) {
        this.clientNote = clientNote;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getClientKeyNote() {
        return clientKeyNote;
    }

    public void setClientKeyNote(String clientKeyNote) {
        this.clientKeyNote = clientKeyNote;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
