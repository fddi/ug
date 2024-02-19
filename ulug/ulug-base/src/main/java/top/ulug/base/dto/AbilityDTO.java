package top.ulug.base.dto;

import java.io.Serializable;

/**
 * Created by liujf on 2021/6/19.
 * 逝者如斯夫 不舍昼夜
 */
public class AbilityDTO implements Serializable {
    private Long abilityId;
    private String projectId;
    private String abilityUri;
    private String abilityNote;
    private String paramsExample;
    private String resultExample;

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
}
