package top.ulug.core.auth.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

/**
 * 用户
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
public class AuthUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 10)
    private String status;

    @Column(unique = true, nullable = false, length = 10)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String userType;

    @Column(length = 50)
    private String nickName;

    @Column(length = 50)
    private String phoneNumber;

    private String address;

    @Column(length = 50)
    private String areaCode;

    @Column(nullable = false, length = 50)
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