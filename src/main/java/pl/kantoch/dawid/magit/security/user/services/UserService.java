package pl.kantoch.dawid.magit.security.user.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.models.AppConstants;
import pl.kantoch.dawid.magit.models.VerificationToken;
import pl.kantoch.dawid.magit.models.events.OnRegistrationCompleteEvent;
import pl.kantoch.dawid.magit.models.exceptions.UserAlreadyExistException;
import pl.kantoch.dawid.magit.models.payloads.requests.SignupRequest;
import pl.kantoch.dawid.magit.repositories.VerificationTokenRepository;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements IUserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final ApplicationEventPublisher eventPublisher;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository, ApplicationEventPublisher eventPublisher)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public User registerNewUserAccount(SignupRequest userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(
                    "Istnieje już konto z podanym adresem email: "
                            + userDto.getEmail());
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        Set<String> strRoles = userDto.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null)
        {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Błąd: nie znaleziono takiej roli!"));
            roles.add(userRole);
        }
        else
        {
            strRoles.forEach(role ->
            {
                switch (role)
                {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public String validateVerificationToken(String token)
    {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return AppConstants.TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return AppConstants.TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return AppConstants.TOKEN_VALID;
    }

    public boolean resendVerificationToken(String expiredToken) {
        VerificationToken vToken = tokenRepository.findByToken(expiredToken);
        if(vToken != null) {
/*            vToken.updateToken(UUID.randomUUID().toString());
            vToken = tokenRepository.save(vToken);*/
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(vToken.getUser(), null, null));
            return true;
        }
        return false;
    }
}
