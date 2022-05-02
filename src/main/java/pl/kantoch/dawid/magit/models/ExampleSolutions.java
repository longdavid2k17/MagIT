package pl.kantoch.dawid.magit.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "example_solutions")
public class ExampleSolutions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "creation_date")
    private Date creationDate;

    public ExampleSolutions() {
    }

    public ExampleSolutions(Long id, Organisation organisation, Task task, String type, String description, Date creationDate) {
        this.id = id;
        this.organisation = organisation;
        this.task = task;
        this.type = type;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "ExampleSolutions{" +
                "id=" + id +
                ", organisation=" + organisation +
                ", task=" + task +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
