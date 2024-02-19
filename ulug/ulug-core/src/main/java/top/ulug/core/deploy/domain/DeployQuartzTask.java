package top.ulug.core.deploy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liujf on 2020-10-28.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"taskGroup", "taskService"})})
public class DeployQuartzTask extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;
    private String taskService;
    @Column(unique = true, nullable = false)
    private String taskName;
    private String taskGroup;
    private String cronExp;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextDate;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskService() {
        return taskService;
    }

    public void setTaskService(String taskService) {
        this.taskService = taskService;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DeployQuartzTask) {
            String name = this.taskName;
            if (name == null || "".equals(name)) {
                return false;
            }
            return name.equals(((DeployQuartzTask) obj).getTaskName());
        }
        return false;
    }
}
