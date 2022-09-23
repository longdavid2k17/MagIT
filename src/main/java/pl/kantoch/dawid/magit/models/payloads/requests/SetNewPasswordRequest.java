package pl.kantoch.dawid.magit.models.payloads.requests;

public class SetNewPasswordRequest
{
    String token;
    String password;

    public SetNewPasswordRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public SetNewPasswordRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
