package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService
{
    private final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder encoder)
    {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public ResponseEntity<?> getUserById(Long id)
    {
        try
        {
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent())
                return ResponseEntity.ok().body(user);
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono użytkownika o ID="+id);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.getUserById for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas pobierania danych użytkownika dla ID="+id+". Komunikat: "+e.getMessage());
        }
    }

    public ResponseEntity<?> save(User user)
    {
        try
        {
            Optional<User> userDb = userRepository.findById(user.getId());
            if(userDb.isPresent())
            {
                if(!user.getPassword().equals(userDb.get().getPassword()))
                    user.setPassword(encoder.encode(user.getPassword()));
                User saved = userRepository.save(user);
                return ResponseEntity.ok().body(saved);
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono użytkownika!");
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.save for entity {}. Message: {}",user.toString(),e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas zapisu profilu! Komunikat: "+e.getMessage());
        }
    }

    public ResponseEntity<?> getByOrganisationId(Long id)
    {
        try
        {
            List<User> user = userRepository.findAllByOrganisation_Id(id);
            return ResponseEntity.ok().body(user);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProfileService.getByCompanyId for id={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas pobierania użytkowników dla organizacji o ID="+id+". Komunikat: "+e.getMessage());
        }
    }
}
