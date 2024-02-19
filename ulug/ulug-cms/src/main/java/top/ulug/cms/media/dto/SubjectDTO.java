package top.ulug.cms.media.dto;

/**
 * Created by liujf on 2020/2/19.
 * 逝者如斯夫 不舍昼夜
 */
public class SubjectDTO {
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private String subjectNote;
    private String subjectType;
    private String logo;
    private String areaCode;
    private String unitCode;
    private Long orgId;
    private String orgName;
    private String status;
    private Integer sort;

    public SubjectDTO(String subjectCode, String subjectName, String subjectNote, String subjectType, String logo) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectNote = subjectNote;
        this.subjectType = subjectType;
        this.logo = logo;
    }

    public SubjectDTO(Long subjectId, String subjectCode, String subjectName, String subjectNote, String subjectType, String logo, String areaCode, String unitCode, String status, Integer sort) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectNote = subjectNote;
        this.subjectType = subjectType;
        this.logo = logo;
        this.areaCode = areaCode;
        this.unitCode = unitCode;
        this.status = status;
        this.sort = sort;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectNote() {
        return subjectNote;
    }

    public void setSubjectNote(String subjectNote) {
        this.subjectNote = subjectNote;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
