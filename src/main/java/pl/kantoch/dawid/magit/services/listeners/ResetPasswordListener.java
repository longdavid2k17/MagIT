package pl.kantoch.dawid.magit.services.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.kantoch.dawid.magit.models.events.OnPasswordResetRequestEvent;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.services.UserService;
import pl.kantoch.dawid.magit.services.MailService;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class ResetPasswordListener implements ApplicationListener<OnPasswordResetRequestEvent>
{
    private final UserService userService;
    private final MailService mailService;

    public ResetPasswordListener(UserService service, MailService mailService)
    {
        this.userService = service;
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(OnPasswordResetRequestEvent event)
    {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createResetPasswordToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Resetowanie hasła";
        String message = "Odnotowanie żądanie zmiany hasła dostępu do systemu MagIT. Aby zresetować hasło kliknij w link poniżej";

        try
        {
            mailService.sendMail(recipientAddress,subject,message + "\r\n" + "http://localhost:4200/reset-confirm?token=" + token,false);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
