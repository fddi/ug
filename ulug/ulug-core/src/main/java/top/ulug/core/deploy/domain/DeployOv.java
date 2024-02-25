package top.ulug.core.deploy.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

/**
 * Created by liujf on 2020/2/9.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"unitCode", "optionCode"})})
public class DeployOv extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ovId;

    @Column(nullable = false, length = 50)
    private String unitCode;

    @Column(nullable = false, length = 50)
    private String optionCode;

    @Column(nullable = false, length = 2000)
    private String optionValue;

    public Long getOvId() {
        return ovId;
    }

    public void setOvId(Long ovId) {
        this.ovId = ovId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }
}
