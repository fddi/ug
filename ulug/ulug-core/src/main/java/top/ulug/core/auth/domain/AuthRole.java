package top.ulug.core.auth.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色岗位
 */
@Entity
@Table
public class AuthRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleId;

    @Column(length = 50)
    private String roleName;

    private String roleNote;

    @Column(length = 50)
    private String roleType;

    private String areaCode;

    @Column(nullable = false, length = 50)
    private String unitCode;

    @Column(insertable = false, updatable = false)
    private Long orgId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "orgId")
    private AuthOrg org;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "auth_role_user", joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<AuthUser> userList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "auth_role_menu", joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "menuId"))
    private List<AuthMenu> menuList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "auth_role_org", joinColumns = @JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "orgId"))
    private List<AuthOrg> orgList = new ArrayList<>();

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

    public String getRoleNote() {
        return roleNote;
    }

    public void setRoleNote(String roleNote) {
        this.roleNote = roleNote;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public AuthOrg getOrg() {
        return org;
    }

    public void setOrg(AuthOrg org) {
        this.org = org;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public List<AuthUser> getUserList() {
        return userList;
    }

    public void setUserList(List<AuthUser> userList) {
        this.userList = userList;
    }

    public List<AuthMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<AuthMenu> menuList) {
        this.menuList = menuList;
    }

    public List<AuthOrg> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<AuthOrg> orgList) {
        this.orgList = orgList;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AuthRole) {
            Long id = this.roleId;
            if (id == null || id == 0L) {
                return false;
            }
            return id.equals(((AuthRole) obj).getRoleId());
        }
        return false;
    }
}