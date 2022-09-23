package pl.kantoch.dawid.magit.models.payloads.requests;

import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.security.user.User;

public class TeamRequestEntity
{
    private Long organisationId;
    private String name;
    private String description;
    private Project defaultProject;
    private User teamLeader;

    public TeamRequestEntity() {
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
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

    public Project getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(Project defaultProject) {
        this.defaultProject = defaultProject;
    }

    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }
}
