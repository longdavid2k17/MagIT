package pl.kantoch.dawid.magit.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.payloads.requests.CreateOrganisationRequest;
import pl.kantoch.dawid.magit.repositories.OrganisationsRepository;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.RandomStringGenerator;

import java.util.Optional;

@Service
public class OrganisationsService
{
    private final OrganisationsRepository organisationsRepository;
    private final UserRepository userRepository;

    public OrganisationsService(OrganisationsRepository organisationsRepository, UserRepository userRepository)
    {
        this.organisationsRepository = organisationsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<?> createOrganisation(CreateOrganisationRequest request)
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

            if(saved.getId() != null)
            {
                user.setOrganisation(saved);
                userRepository.save(user);
            }

            return ResponseEntity.ok().body("Utworzono organizację!");
        }
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Brak danych!");
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
}
