package pl.kantoch.dawid.magit.security.user.services;

import com.google.gson.Gson;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.AppConstants;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.PasswordResetToken;
import pl.kantoch.dawid.magit.models.VerificationToken;
import pl.kantoch.dawid.magit.models.events.OnPasswordResetRequestEvent;
import pl.kantoch.dawid.magit.models.events.OnRegistrationCompleteEvent;
import pl.kantoch.dawid.magit.models.exceptions.UserAlreadyExistException;
import pl.kantoch.dawid.magit.models.payloads.requests.SignupRequest;
import pl.kantoch.dawid.magit.repositories.OrganisationsRepository;
import pl.kantoch.dawid.magit.repositories.PasswordResetTokenRepository;
import pl.kantoch.dawid.magit.repositories.VerificationTokenRepository;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.*;

@Service
public class UserService implements IUserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;
    private final OrganisationsRepository organisationsRepository;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository, PasswordEncoder encoder,
                       ApplicationEventPublisher eventPublisher, OrganisationsRepository organisationsRepository)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.encoder = encoder;
        this.eventPublisher = eventPublisher;
        this.organisationsRepository = organisationsRepository;
    }

    @Override
    public User registerNewUserAccount(SignupRequest userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(
                    "Istnieje już konto z podanym adresem email: "
                            + userDto.getEmail());
        }
        if (loginExist(userDto.getUsername())) {
            throw new UserAlreadyExistException(
                    "Istnieje już konto z podaną nazwą użytkownika: "
                            + userDto.getUsername());
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
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
                    case "pm":
                        Role pmRole = roleRepository.findByName(ERole.ROLE_PM)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(pmRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        if(userDto.getInviteCode()!=null)
        {
            Organisation organisation = organisationsRepository.findByInviteCode(userDto.getInviteCode());
            if(organisation!=null)
                user.setOrganisation(organisation);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<?> grantPmRole(Long id)
    {
        try
        {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono użytkownika o ID="+id);
            User user = optionalUser.get();
            Role pmRole = roleRepository.findByName(ERole.ROLE_PM)
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono roli PM w bazie danych."));
            if(!user.getRoles().contains(pmRole))
            {
                Set<Role> userRoles = user.getRoles();
                userRoles.add(pmRole);
                user.setRoles(userRoles);
                userRepository.save(user);
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby dodania nowej roli uzytkownikowi o ID="+id+"! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> removePmRole(Long id)
    {
        try
        {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono użytkownika o ID="+id);
            User user = optionalUser.get();
            Role pmRole = roleRepository.findByName(ERole.ROLE_PM)
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono roli PM w bazie danych."));
            if(user.getRoles().contains(pmRole))
            {
                Set<Role> userRoles = user.getRoles();
                userRoles.remove(pmRole);
                user.setRoles(userRoles);
                userRepository.save(user);
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby usunięcia roli PM uzytkownikowi o ID="+id+"! Komunikat: "+e.getMessage()));
        }
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

    private boolean loginExist(String login) {
        return userRepository.findByUsername(login).isPresent();
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

    @Transactional
    public ResponseEntity<?> resetPassword(String email)
    {
        User optionalUser = userRepository.findByEmail(email);
        if(optionalUser!=null)
        {
            optionalUser.setEnabled(false);
            userRepository.save(optionalUser);
            eventPublisher.publishEvent(new OnPasswordResetRequestEvent(optionalUser));
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika z adresem email "+email));
    }

    @Transactional
    public void createResetPasswordToken(User user, String token)
    {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Transactional
    public ResponseEntity<?> setNewPasword(String token, String password)
    {
        try
        {
            PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
            if(resetToken!=null)
            {
                if(resetToken.getExpiryDate().after(new Date()))
                {
                    User user = resetToken.getUser();
                    user.setEnabled(true);
                    user.setPassword(encoder.encode(password));
                    userRepository.save(user);
                    List<PasswordResetToken> allTokens = passwordResetTokenRepository.findAllByUser_Id(user.getId());
                    passwordResetTokenRepository.deleteAll(allTokens);
                    return ResponseEntity.ok().build();
                }
                else
                {
                    List<PasswordResetToken> allTokens = passwordResetTokenRepository.findAllByUser_Id(resetToken.getUser().getId());
                    passwordResetTokenRepository.deleteAll(allTokens);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Link stracił ważność! Spróbuj ponownie zresetować hasło!"));
                }
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby resetowania hasła! Przesłano niepoprawne żądanie!"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas próby resetowania hasła! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public void updateLastLoggedDateForUser(String username)
    {
        try
        {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isPresent())
            {
                User user = optionalUser.get();
                user.setLastLogged(new Date());
                userRepository.save(user);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
