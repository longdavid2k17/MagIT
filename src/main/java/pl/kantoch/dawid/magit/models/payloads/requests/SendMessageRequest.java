package pl.kantoch.dawid.magit.models.payloads.requests;

public class SendMessageRequest
{
    private Long userId;
    private Long interlocutorId;
    private String text;

    public SendMessageRequest(Long userId, Long interlocutorId, String text) {
        this.userId = userId;
        this.interlocutorId = interlocutorId;
        this.text = text;
    }

    public SendMessageRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInterlocutorId() {
        return interlocutorId;
    }

    public void setInterlocutorId(Long interlocutorId) {
        this.interlocutorId = interlocutorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
