package top.ulug.core.deploy.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 服务接口能力
 */
@Entity
@Table
public class DeployAbility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long abilityId;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '项目名称'")
    private String projectId;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(128) COMMENT '接口地址'")
    private String abilityUri;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '接口说明'")
    private String abilityNote;

    @Column(columnDefinition = "VARCHAR(1024) COMMENT '参数示例'")
    private String paramsExample;

    @Column(columnDefinition = "VARCHAR(1024) COMMENT '返回示例'")
    private String resultExample;

    private String status;

    public Long getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(Long abilityId) {
        this.abilityId = abilityId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAbilityUri() {
        return abilityUri;
    }

    public void setAbilityUri(String abilityUri) {
        this.abilityUri = abilityUri;
    }

    public String getAbilityNote() {
        return abilityNote;
    }

    public void setAbilityNote(String abilityNote) {
        this.abilityNote = abilityNote;
    }

    public String getParamsExample() {
        return paramsExample;
    }

    public void setParamsExample(String paramsExample) {
        this.paramsExample = paramsExample;
    }

    public String getResultExample() {
        return resultExample;
    }

    public void setResultExample(String resultExample) {
        this.resultExample = resultExample;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DeployAbility) {
            Long id = this.abilityId;
            if (id == null || id == 0L) {
                return false;
            }
            return id.equals(((DeployAbility) obj).getAbilityId());
        }
        return false;
    }
}