package top.ulug.cms.media.dto;

import java.io.Serializable;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
public class NetDiskDTO implements Serializable {
    private Long diskId;
    private Long subjectId;
    private String diskName;
    private long diskSize;
    private long usedSize;

    public NetDiskDTO() {

    }

    public NetDiskDTO(Long diskId, Long subjectId, String diskName, long diskSize, long usedSize) {
        this.diskId = diskId;
        this.subjectId = subjectId;
        this.diskName = diskName;
        this.diskSize = diskSize;
        this.usedSize = usedSize;
    }

    public Long getDiskId() {
        return diskId;
    }

    public void setDiskId(Long diskId) {
        this.diskId = diskId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(long diskSize) {
        this.diskSize = diskSize;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(long usedSize) {
        this.usedSize = usedSize;
    }
}
