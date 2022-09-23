package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "memory_directories")
public class MemoryDirectory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String generatedUrl;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Date creationDate;

    public MemoryDirectory() {
    }

    public MemoryDirectory(Long id, String generatedUrl, Organisation organisation, Project project, Date creationDate) {
        this.id = id;
        this.generatedUrl = generatedUrl;
        this.organisation = organisation;
        this.project = project;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeneratedUrl() {
        return generatedUrl;
    }

    public void setGeneratedUrl(String generatedUrl) {
        this.generatedUrl = generatedUrl;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "MemoryDirectory{" +
                "id=" + id +
                ", generatedUrl='" + generatedUrl + '\'' +
                ", organisation=" + organisation +
                ", project=" + project +
                ", creationDate=" + creationDate +
                '}';
    }
}
