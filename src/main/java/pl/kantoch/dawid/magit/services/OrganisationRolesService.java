package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.repositories.OrganisationRolesRepository;

import java.util.List;

@Service
public class OrganisationRolesService
{
    private final Logger LOGGER = LoggerFactory.getLogger(OrganisationRolesService.class);

    private final OrganisationRolesRepository organisationRolesRepository;

    public OrganisationRolesService(OrganisationRolesRepository organisationRolesRepository)
    {
        this.organisationRolesRepository = organisationRolesRepository;
    }

    public ResponseEntity<?> getRolesForOrganisation(Long id)
    {
        try
        {
            List<OrganisationRole> roles = organisationRolesRepository.findAllByOrganisationId(id);
            return ResponseEntity.ok().body(roles);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in OrganisationRolesService.getRolesForOrganisation for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd podczas pobierania ról organizacji! Komunikat: "+e.getMessage());
        }
    }

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
}
