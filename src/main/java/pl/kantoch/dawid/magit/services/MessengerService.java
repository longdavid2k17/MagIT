package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessengerService
{
    private final Logger LOGGER = LoggerFactory.getLogger(MessengerService.class);

    private final UserRepository userRepository;

    public MessengerService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getAllContacts(Long id)
    {
        try
        {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isPresent())
            {
                List<User> users = userRepository.findAllByOrganisation_Id(optionalUser.get().getOrganisation().getId());
                users.remove(optionalUser.get());
                return ResponseEntity.ok().body(users);
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono użytkownika o ID="+id);
        }
        catch (Exception e)
        {
            LOGGER.error("Error MessengerService.getAllContacts for ID={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas pobierania listy kontaktów! Komunikat: "+e.getMessage());
        }
    }
}
