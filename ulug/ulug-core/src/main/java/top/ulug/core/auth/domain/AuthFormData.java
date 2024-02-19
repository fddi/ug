package top.ulug.core.auth.domain;

import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by liujf on 2021/5/25.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class AuthFormData extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long formId;

    private String formName;

    @Column(unique = true)
    private String formCode;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String formMapper;

    @Column(columnDefinition = "VARCHAR(2) COMMENT '状态 1--正常 0--异常'")
    private String status;

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormMapper() {
        return formMapper;
    }

    public void setFormMapper(String formMapper) {
        this.formMapper = formMapper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}