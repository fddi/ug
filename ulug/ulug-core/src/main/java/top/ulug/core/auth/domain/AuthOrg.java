package top.ulug.core.auth.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

/**
 * 组织结构
 */
@Entity
@Table
public class AuthOrg extends BaseEntity implements Comparable<AuthOrg> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orgId;

    @Column(length = 10)
    private String status;

    @Column(nullable = false, unique = true,length = 50)
    private String orgCode;

    private String orgName;

    @Column(length = 50)
    private String orgType;

    @Column(nullable = false, length = 50)
    private String areaCode;

    @Column(nullable = false, length = 50)
    private String unitCode;

    @Column(nullable = false)
    private Long parentId;

    @Column(length = 50)
    private String parentCode;

    private String orgPath;

    private Integer orgSort;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public Integer getOrgSort() {
        return orgSort;
    }

    public void setOrgSort(Integer orgSort) {
        this.orgSort = orgSort;
    }

    @Override
    public int compareTo(AuthOrg org) {
        return this.getOrgSort().compareTo(org.getOrgSort());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AuthOrg) {
            Long id = this.orgId;
            if (id == null || id == 0L) {
                return false;
            }
            return id.equals(((AuthOrg) obj).getOrgId());
        }
        return false;
    }
}