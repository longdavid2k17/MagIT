package pl.kantoch.dawid.magit.services.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import pl.kantoch.dawid.magit.models.events.OnRegistrationCompleteEvent;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.services.IUserService;
import pl.kantoch.dawid.magit.services.MailService;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    private final IUserService service;
    private final MessageSource messages;
    private final MailService mailService;

    public RegistrationListener(IUserService service, MessageSource messages, MailService mailService)
    {
        this.service = service;
        this.messages = messages;
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Potwierdź rejestrację";
/*        String confirmationUrl
                = event.getAppUrl() + "/register-confirm?token=" + token;*/
        String confirmationUrl
                = event.getAppUrl() + "/register-confirm?token=" + token;
        String message = "Pomyślnie zarejestrowano się do systemu MagIT. Aby aktywować konto kliknij w link poniżej";

        mailService.sendMail(recipientAddress,subject,message + "\r\n" + "http://localhost:4200/register-confirm?token=" + token,false);
    }
}
