package top.ulug.core.auth.dto;

/**
 * Created by liujf on 2019/10/17.
 * 逝者如斯夫 不舍昼夜
 */
public class UserDTO {
    private String orgCode;
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String address;
    private Long orgId;
    private String areaCode;
    private String unitCode;
    private String userType;
    private Long userId;
    private String status;
    private String orgName;
    public UserDTO(Long userId, String status, String userName, String userType, String nickName, String phoneNumber, String address, String areaCode, String unitCode, Long orgId, String orgName) {
        this.userName = userName;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orgId = orgId;
        this.areaCode = areaCode;
        this.unitCode = unitCode;
        this.userType = userType;
        this.userId = userId;
        this.status = status;
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
