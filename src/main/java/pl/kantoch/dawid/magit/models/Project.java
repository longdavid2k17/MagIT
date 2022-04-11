package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.kantoch.dawid.magit.security.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "drive_name")
    private String driveName;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "archived")
    private Boolean archived;

    @CreationTimestamp
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Column(name = "modification_date")
    private Date modificationDate;

    public Project() {
    }

    public Project(Long id, String name, String description, String driveName, User projectManager, Organisation organisation, Date startDate, Date endDate, Boolean deleted, Boolean archived, Date createDate, Date modificationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.driveName = driveName;
        this.projectManager = projectManager;
        this.organisation = organisation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deleted = deleted;
        this.archived = archived;
        this.createDate = createDate;
        this.modificationDate = modificationDate;
    }

    public Project(Long id, String name, String description, String driveName, User projectManager, Organisation organisation, Date startDate, Date endDate, Boolean deleted, Boolean archived) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.driveName = driveName;
        this.projectManager = projectManager;
        this.organisation = organisation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deleted = deleted;
        this.archived = archived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", driveName='" + driveName + '\'' +
                ", projectManager=" + projectManager +
                ", organisation=" + organisation +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", deleted=" + deleted +
                ", archived=" + archived +
                ", createDate=" + createDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
