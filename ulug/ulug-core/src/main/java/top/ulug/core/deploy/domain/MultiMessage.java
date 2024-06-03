package top.ulug.core.deploy.domain;

import jakarta.persistence.*;
import top.ulug.base.auditor.BaseEntity;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:04 星期五
 */
@Entity
@Table
public class MultiMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageId;

    @Column(length = 50)
    private String title;

    @Column(length = 200)
    private String message;

    @Column(length = 200)
    private String actionUrl;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;

    private String status;

    private String level;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
