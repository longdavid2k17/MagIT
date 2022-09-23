package pl.kantoch.dawid.magit.models.payloads.requests;

public class CreateOrganisationRequest
{
    private String login;
    private String name;
    private String description;

    public CreateOrganisationRequest(String login, String name, String description) {
        this.login = login;
        this.name = name;
        this.description = description;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
}
