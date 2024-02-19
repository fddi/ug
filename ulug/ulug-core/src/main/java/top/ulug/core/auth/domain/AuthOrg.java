package top.ulug.core.auth.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 组织结构
 */
@Entity
@Table
public class AuthOrg extends BaseEntity implements Comparable<AuthOrg> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orgId;

    @Column(columnDefinition = "VARCHAR(2) COMMENT '状态 1--正常 0--异常'")
    private String status;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(16) COMMENT '机构代码'")
    private String orgCode;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '机构名'")
    private String orgName;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '机构类型'")
    private String orgType;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '地区代码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    @Column(nullable = false, columnDefinition = "INT(10) COMMENT '父级id'")
    private Long parentId;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '附加值'")
    private String parentCode;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '路径'")
    private String orgPath;

    @Column(columnDefinition = "INT(5) COMMENT '机构排序'")
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