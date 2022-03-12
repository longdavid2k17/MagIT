package pl.kantoch.dawid.magit.security.user.services;

import pl.kantoch.dawid.magit.models.VerificationToken;
import pl.kantoch.dawid.magit.models.exceptions.UserAlreadyExistException;
import pl.kantoch.dawid.magit.models.payloads.requests.SignupRequest;
import pl.kantoch.dawid.magit.security.user.User;

public interface IUserService
{
    User registerNewUserAccount(SignupRequest userDto)
            throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
}
