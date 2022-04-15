package pl.kantoch.dawid.magit.models.payloads.requests;

import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.security.user.User;

public class TeamMemberRequestEntity
{
    private OrganisationRole role;
    private User user;

    public TeamMemberRequestEntity() {
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
}
