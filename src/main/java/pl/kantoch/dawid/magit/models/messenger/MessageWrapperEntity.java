package pl.kantoch.dawid.magit.models.messenger;

import pl.kantoch.dawid.magit.models.Message;
import pl.kantoch.dawid.magit.security.user.User;

import java.util.List;

public class MessageWrapperEntity
{
    private Long unread;
    private User interlocutor;
    private List<Message> messages;

    public MessageWrapperEntity() {
    }

    public MessageWrapperEntity(Long unread, User interlocutor, List<Message> messages) {
        this.unread = unread;
        this.interlocutor = interlocutor;
        this.messages = messages;
    }

    public Long getUnread() {
        return unread;
    }

    public void setUnread(Long unread) {
        this.unread = unread;
    }

    public User getInterlocutor() {
        return interlocutor;
    }

    public void setInterlocutor(User interlocutor) {
        this.interlocutor = interlocutor;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
