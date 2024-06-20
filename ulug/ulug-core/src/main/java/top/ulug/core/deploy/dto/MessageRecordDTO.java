package top.ulug.core.deploy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liu
 * @Date 2024/6/20 下午8:59 星期四
 */
public class MessageRecordDTO implements Serializable {
    private Long messageId;
    private String title;
    private String message;
    private String actionUrl;
    private Long recordId;
    private String userName;
    private String readStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date msgTime;

    public MessageRecordDTO(Long messageId, String title, String message, String actionUrl, Long recordId, String userName, String readStatus, Date msgTime) {
        this.messageId = messageId;
        this.title = title;
        this.message = message;
        this.actionUrl = actionUrl;
        this.recordId = recordId;
        this.userName = userName;
        this.readStatus = readStatus;
        this.msgTime = msgTime;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

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

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }
}
