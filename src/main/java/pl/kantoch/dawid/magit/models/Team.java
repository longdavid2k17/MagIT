package pl.kantoch.dawid.magit.models;

import pl.kantoch.dawid.magit.security.user.User;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class Team
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private User teamLeader;

    @ManyToOne
    @JoinColumn(name = "default_project_id")
    private Project defaultProject;

    @Column(name = "organisation_id")
    private Long organisationId;

    @Column(name = "deleted")
    private Boolean deleted;

    @Transient
    private String teamTasks;

    @Transient
    private String individualTasks;

    public Team() {
    }

    public Team(Long id, String name, String description, User teamLeader, Project defaultProject, Long organisationId, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teamLeader = teamLeader;
        this.defaultProject = defaultProject;
        this.organisationId = organisationId;
        this.deleted = deleted;
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

    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Project getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(Project defaultProject) {
        this.defaultProject = defaultProject;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getTeamTasks() {
        return teamTasks;
    }

    public void setTeamTasks(String teamTasks) {
        this.teamTasks = teamTasks;
    }

    public String getIndividualTasks() {
        return individualTasks;
    }

    public void setIndividualTasks(String individualTasks) {
        this.individualTasks = individualTasks;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teamLeader=" + teamLeader +
                ", defaultProject=" + defaultProject +
                ", organisationId=" + organisationId +
                ", deleted=" + deleted +
                '}';
    }
}
