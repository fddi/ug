package top.ulug.core.auth.dto;

import top.ulug.core.auth.domain.AuthOrg;

/**
 * Created by liujf on 2019/10/17.
 * 逝者如斯夫 不舍昼夜
 */
public class OrgDTO {
    private String orgName;
    private String orgType;
    private String orgCode;
    private String areaCode;
    private String unitCode;
    private String parentCode;
    private Integer orgSort;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getOrgSort() {
        return orgSort;
    }

    public void setOrgSort(Integer orgSort) {
        this.orgSort = orgSort;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public AuthOrg convert() {
        AuthOrg org = new AuthOrg();
        org.setParentId(0L);
        org.setOrgName(this.orgName);
        org.setOrgType(this.orgType);
        org.setOrgCode(this.orgCode);
        org.setAreaCode(this.areaCode);
        org.setUnitCode(this.unitCode);
        org.setOrgSort(this.orgSort);
        org.setParentCode(this.parentCode);
        return org;
    }
}
