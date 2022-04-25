package pl.kantoch.dawid.magit;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task_result")
public class TaskResult
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "result_type")
    private String resultType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;

    @Column(name = "author")
    private String login;

    public TaskResult() {
    }

    public TaskResult(Long id, String resultType, String fileUrl, Long taskId, Date creationDate, String login) {
        this.id = id;
        this.resultType = resultType;
        this.fileUrl = fileUrl;
        this.taskId = taskId;
        this.creationDate = creationDate;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "id=" + id +
                ", resultType='" + resultType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", taskId=" + taskId +
                ", creationDate=" + creationDate +
                ", login='" + login + '\'' +
                '}';
    }
}
