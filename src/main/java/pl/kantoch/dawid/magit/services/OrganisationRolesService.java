package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.repositories.OrganisationRolesRepository;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisationRolesService
{
    private final Logger LOGGER = LoggerFactory.getLogger(OrganisationRolesService.class);

    private final OrganisationRolesRepository organisationRolesRepository;
    private final UserRepository userRepository;

    public OrganisationRolesService(OrganisationRolesRepository organisationRolesRepository, UserRepository userRepository)
    {
        this.organisationRolesRepository = organisationRolesRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getRolesForOrganisation(Long id, Pageable pageable)
    {
        try
        {
            Page<OrganisationRole> roles = organisationRolesRepository.findAllByOrganisationId(id,pageable);
            return ResponseEntity.ok().body(roles);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in OrganisationRolesService.getRolesForOrganisation for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas pobierania ról organizacji! Komunikat: "+e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> save(OrganisationRole role)
    {
        try
        {
            OrganisationRole saved = organisationRolesRepository.save(role);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in OrganisationRolesService.save for entity {}. Message: {}",role.toString(),e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas zapisu roli! Komunikat: "+e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> saveForUser(Long id, List<OrganisationRole> roles)
    {
        try
        {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas walidacji! Nie znaleziono użytkownika o ID="+id);
            if(roles.stream().anyMatch(e -> e.getId() == null))
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas walidacji! Wybrano nieprawidłowe role do zapisu!");
            User user = optionalUser.get();
            user.setOrganisationRoles(new HashSet<>(roles));
            userRepository.save(user);
            return ResponseEntity.ok().body("Poprawnie zapisano role!");
        }
        catch (Exception e)
        {
            LOGGER.error("Error in OrganisationRolesService.saveForUser for id = {} and roles = {}. Message: {}",id,roles.toString(),e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas zapisu ról! Komunikat: "+e.getMessage());
        }
    }
}
