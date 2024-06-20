package top.ulug.core.deploy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import top.ulug.base.auditor.BaseEntity;

import java.util.Date;

/**
 * @Author liu
 * @Date 2024/5/10 下午3:41 星期五
 */
@Entity
@Table
public class MessageRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recordId;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(length = 5)
    private String sendStatus;

    private String readStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTime;

    private Long multiMessageId;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Long getMultiMessageId() {
        return multiMessageId;
    }

    public void setMultiMessageId(Long multiMessageId) {
        this.multiMessageId = multiMessageId;
    }
}
