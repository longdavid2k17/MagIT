package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.kantoch.dawid.magit.security.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @Column(name = "github_url")
    private String gitHubUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "completed")
    @NotNull
    private Boolean completed;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Date creationDate;

    @UpdateTimestamp
    @Column(name = "modification_date")
    private Date modificationDate;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "deadline_date")
    private Date deadlineDate;

    @Column(name = "is_deleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    public Task() {
    }

    public Task(Long id, String title, String description, Team team, Project project, User user, Task parentTask, String gitHubUrl, String status, Boolean completed, Date creationDate, Date modificationDate, Date startDate, Date deadlineDate, Boolean deleted, Organisation organisation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.team = team;
        this.project = project;
        this.user = user;
        this.parentTask = parentTask;
        this.gitHubUrl = gitHubUrl;
        this.status = status;
        this.completed = completed;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
        this.deleted = deleted;
        this.organisation = organisation;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public String getGitHubUrl() {
        return gitHubUrl;
    }

    public void setGitHubUrl(String gitHubUrl) {
        this.gitHubUrl = gitHubUrl;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", team=" + team +
                ", project=" + project +
                ", user=" + user +
                ", parentTask=" + parentTask +
                ", gitHubUrl='" + gitHubUrl + '\'' +
                ", status='" + status + '\'' +
                ", completed=" + completed +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", startDate=" + startDate +
                ", deadlineDate=" + deadlineDate +
                ", deleted=" + deleted +
                ", organisation=" + organisation +
                '}';
    }
}
