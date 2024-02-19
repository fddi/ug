package top.ulug.core.auth.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * 用户
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class AuthUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(columnDefinition = "VARCHAR(2) COMMENT '状态 1--正常 0--异常'")
    private String status;

    @Column(unique = true, nullable = false,
            columnDefinition = "VARCHAR(50) NOT NULL COMMENT '用户名'")
    private String userName;

    @Column(nullable = false, columnDefinition = "VARCHAR(128) NOT NULL COMMENT '密码'")
    private String password;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '用户类型'")
    private String userType;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '名称'")
    private String nickName;

    @Column(columnDefinition = "VARCHAR(16) COMMENT '手机号'")
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(128) COMMENT '地址'")
    private String address;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '区域编码'")
    private String areaCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '单位代码'")
    private String unitCode;

    @Column(nullable = false, insertable = false, updatable = false)
    private Long orgId;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "orgId")
    private AuthOrg org;

    public AuthUser() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
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

    public AuthOrg getOrg() {
        return org;
    }

    public void setOrg(AuthOrg org) {
        this.org = org;
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
        if (obj instanceof AuthUser) {
            Long id = this.userId;
            if (id == null || id == 0L) {
                return false;
            }
            return id.equals(((AuthUser) obj).getUserId());
        }
        return false;
    }
}