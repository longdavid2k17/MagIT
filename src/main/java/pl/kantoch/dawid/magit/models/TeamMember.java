package pl.kantoch.dawid.magit.models;

import pl.kantoch.dawid.magit.security.user.User;

import javax.persistence.*;

@Entity
@Table(name = "team_members")
public class TeamMember
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private OrganisationRole role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public TeamMember() {
    }

    public TeamMember(Long id, Team team, OrganisationRole role, User user) {
        this.id = id;
        this.team = team;
        this.role = role;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public OrganisationRole getRole() {
        return role;
    }

    public void setRole(OrganisationRole role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "id=" + id +
                ", team=" + team +
                ", role=" + role +
                ", user=" + user +
                '}';
    }
}
