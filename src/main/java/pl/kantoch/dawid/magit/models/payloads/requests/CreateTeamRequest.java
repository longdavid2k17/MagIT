package pl.kantoch.dawid.magit.models.payloads.requests;

import java.util.List;

public class CreateTeamRequest
{
    private TeamRequestEntity team;
    private List<TeamMemberRequestEntity> teamMembers;

    public CreateTeamRequest() {
    }

    public CreateTeamRequest(TeamRequestEntity team, List<TeamMemberRequestEntity> teamMembers) {
        this.team = team;
        this.teamMembers = teamMembers;
    }

    public TeamRequestEntity getTeam() {
        return team;
    }

    public void setTeam(TeamRequestEntity team) {
        this.team = team;
    }

    public List<TeamMemberRequestEntity> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TeamMemberRequestEntity> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
