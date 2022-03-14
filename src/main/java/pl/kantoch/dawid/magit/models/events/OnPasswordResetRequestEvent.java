package pl.kantoch.dawid.magit.models.events;

import org.springframework.context.ApplicationEvent;
import pl.kantoch.dawid.magit.security.user.User;

public class OnPasswordResetRequestEvent extends ApplicationEvent
{
    private User user;

    public OnPasswordResetRequestEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
