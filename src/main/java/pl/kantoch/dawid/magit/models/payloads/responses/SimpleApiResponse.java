package pl.kantoch.dawid.magit.models.payloads.responses;

public class SimpleApiResponse
{
    private Boolean success;
    private String message;

    public SimpleApiResponse() {
    }

    public SimpleApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
