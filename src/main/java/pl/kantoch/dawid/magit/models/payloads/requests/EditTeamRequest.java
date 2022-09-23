package pl.kantoch.dawid.magit.models.payloads.requests;

import pl.kantoch.dawid.magit.models.Team;
import pl.kantoch.dawid.magit.models.TeamMember;

import java.util.List;

public class EditTeamRequest
{
    private Team team;
    private List<TeamMember> teamMembers;

    public EditTeamRequest() {
    }

    public EditTeamRequest(Team team, List<TeamMember> teamMembers) {
        this.team = team;
        this.teamMembers = teamMembers;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
