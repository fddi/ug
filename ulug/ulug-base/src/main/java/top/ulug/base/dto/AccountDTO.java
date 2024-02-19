package top.ulug.base.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */
public class AccountDTO implements Serializable {
    private String token;
    private long authTime;
    private Long userId;
    private String status;
    private String userName;
    private String userType;
    private String nickName;
    private String phoneNumber;
    private String address;
    private Long orgId;
    private String orgName;
    private String areaCode;
    private String unitCode;

    public AccountDTO() {

    }

    public AccountDTO(Long userId, String status, String userName, String userType,
                      String nickName, String phoneNumber, String address, String areaCode,
                      String unitCode, Long orgId, String orgName) {
        this.userId = userId;
        this.status = status;
        this.userName = userName;
        this.userType = userType;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.areaCode = areaCode;
        this.unitCode = unitCode;
        this.orgId = orgId;
        this.orgName = orgName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(long authTime) {
        this.authTime = authTime;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
