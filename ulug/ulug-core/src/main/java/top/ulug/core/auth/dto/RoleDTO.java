package top.ulug.core.auth.dto;

import java.io.Serializable;

/**
 * Created by liujf on 2019/10/31.
 * 逝者如斯夫 不舍昼夜
 */
public class RoleDTO implements Serializable {
    private Long roleId;
    private String roleName;
    private String roleNote;
    private String roleType;
    private String areaCode;
    private String unitCode;
    private Long orgId;
    private String orgName;

    public RoleDTO() {

    }

    public RoleDTO(Long roleId, String roleName,
                   String roleNote,
                   String roleType, Long orgId, String orgName) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleNote = roleNote;
        this.roleType = roleType;
        this.orgId = orgId;
        this.orgName = orgName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNote() {
        return roleNote;
    }

    public void setRoleNote(String roleNote) {
        this.roleNote = roleNote;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
