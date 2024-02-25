package top.ulug.core.deploy.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

/**
 * 服务接口能力
 */
@Entity
@Table
public class DeployAbility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long abilityId;

    private String projectId;

    @Column(unique = true, nullable = false)
    private String abilityUri;

    private String abilityNote;

    @Column(length = 2000)
    private String paramsExample;

    @Column(length = 2000)
    private String resultExample;

    @Column(length = 10)
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