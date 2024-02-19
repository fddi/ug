package top.ulug.cms.media.dto;

/**
 * Created by liujf on 2020-04-29.
 * 逝者如斯夫 不舍昼夜
 */
public class HotAppDTO {
    private Long hotId;
    private String hotKey;
    private Long subjectId;
    private String hotName;
    private String hotCode;
    private String hotNote;
    private String hotType;
    private String version;
    private String logo;
    private String areaCode;
    private String unitCode;
    private String publishStatus;
    private String publishUri;

    public HotAppDTO(Long hotId, String hotKey, Long subjectId, String hotName, String hotCode, String hotNote, String hotType, String version, String logo, String areaCode, String unitCode, String publishStatus, String publishUri) {
        this.hotId = hotId;
        this.hotKey = hotKey;
        this.subjectId = subjectId;
        this.hotName = hotName;
        this.hotCode = hotCode;
        this.hotNote = hotNote;
        this.hotType = hotType;
        this.version = version;
        this.logo = logo;
        this.areaCode = areaCode;
        this.unitCode = unitCode;
        this.publishStatus = publishStatus;
        this.publishUri = publishUri;
    }

    public Long getHotId() {
        return hotId;
    }

    public void setHotId(Long hotId) {
        this.hotId = hotId;
    }

    public String getHotKey() {
        return hotKey;
    }

    public void setHotKey(String hotKey) {
        this.hotKey = hotKey;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getHotName() {
        return hotName;
    }

    public void setHotName(String hotName) {
        this.hotName = hotName;
    }

    public String getHotCode() {
        return hotCode;
    }

    public void setHotCode(String hotCode) {
        this.hotCode = hotCode;
    }

    public String getHotNote() {
        return hotNote;
    }

    public void setHotNote(String hotNote) {
        this.hotNote = hotNote;
    }

    public String getHotType() {
        return hotType;
    }

    public void setHotType(String hotType) {
        this.hotType = hotType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getPublishUri() {
        return publishUri;
    }

    public void setPublishUri(String publishUri) {
        this.publishUri = publishUri;
    }
}
