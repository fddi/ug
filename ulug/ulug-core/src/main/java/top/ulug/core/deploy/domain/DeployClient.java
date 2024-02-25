package top.ulug.core.deploy.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

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

    @Column(length = 10)
    private String status;

    @Column(unique = true, nullable = false)
    private String clientName;

    @Column(unique = true, nullable = false)
    private String clientKey;

    private String clientKeyNote;

    @Column(length = 50)
    private String clientType;

    private String clientNote;

    @Column(length = 50)
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
