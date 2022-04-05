package pl.kantoch.dawid.magit.services;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.payloads.requests.CreateOrganisationRequest;
import pl.kantoch.dawid.magit.repositories.OrganisationsRepository;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.RandomStringGenerator;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Service
public class OrganisationsService
{
    private final Logger LOGGER = LoggerFactory.getLogger(OrganisationsService.class);

    private final OrganisationsRepository organisationsRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static final Gson gson = new Gson();

    public OrganisationsService(OrganisationsRepository organisationsRepository, UserRepository userRepository,
                                RoleRepository roleRepository)
    {
        this.organisationsRepository = organisationsRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ResponseEntity<String> createOrganisation(CreateOrganisationRequest request)
    {
        try
        {
            if(request!=null)
            {
                if(request.getLogin()==null || request.getName()==null)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Brak nazwy organizacji!");
                Organisation organisation = new Organisation();
                organisation.setName(request.getName());
                organisation.setDescription(request.getDescription());
                organisation.setInviteCode(generateInviteCode());
                Optional<User> optionalUser = userRepository.findByUsername(request.getLogin());
                User user = null;
                if(optionalUser.isPresent())
                {
                    user = optionalUser.get();
                    organisation.setOwnerId(user.getId());
                }
                Organisation saved = organisationsRepository.save(organisation);

                if(saved.getId() != null && user!=null)
                {
                    Set<Role> userRoles = user.getRoles();
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Błąd: nie znaleziono takiej roli!"));
                    Role pmRole = roleRepository.findByName(ERole.ROLE_PM).orElseThrow(() -> new RuntimeException("Błąd: nie znaleziono takiej roli!"));
                    userRoles.addAll(Arrays.asList(adminRole,pmRole));
                    user.setRoles(userRoles);
                    user.setOrganisation(saved);
                    userRepository.save(user);
                }

                return ResponseEntity.ok().body(gson.toJson("Utworzono organizację!"));
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Brak danych!"));
        }
        catch (Exception exception)
        {
            LOGGER.error("Error: {} in OrganisationsService.createOrganisation()",exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Wystąpił błąd podczas tworzenia nowej organizacji! Komunikat: "+exception.getMessage()));
        }
    }

    public String generateInviteCode()
    {
        try
        {
            String inviteCode = null;
            boolean isFree = false;
            while (!isFree)
            {
                String tempCode = RandomStringGenerator.generateRandomString(10);
                Organisation organisation = organisationsRepository.findByInviteCode(tempCode);
                if(organisation==null)
                {
                    inviteCode=tempCode;
                    isFree=true;
                }
            }
            return inviteCode;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> getOrganisationByOwnerId(Long id)
    {
        try
        {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findOrganisationByOwnerId(id);
            if(optionalOrganisation.isPresent())
                return ResponseEntity.ok().body(optionalOrganisation.get());
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie znaleziono organizacji, której właścicielem jest użytkownik o ID="+id);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in OrganisationsService.getOrganisationByOwnerId for ID={}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas pobierania danych dla użytkownika o ID="+id+". Komunikat: "+e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> save(Organisation organisation)
    {
        try
        {
            if(organisation.getInviteCode()==null)
                organisation.setInviteCode(generateInviteCode());
            Organisation saved = organisationsRepository.save(organisation);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in OrganisationsService.save for entity {}. Message: {}",organisation.toString(),e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas zapisu danych organizacji! Komunikat: "+e.getMessage());
        }
    }
}
