package pl.kantoch.dawid.magit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.services.OrganisationRolesService;

import java.util.List;

@RestController
@RequestMapping("/api/organisation-roles")
public class OrganisationRolesController
{
    private final OrganisationRolesService organisationRolesService;

    public OrganisationRolesController(OrganisationRolesService organisationRolesService)
    {
        this.organisationRolesService = organisationRolesService;
    }

    @GetMapping("/organisation/{id}")
    public ResponseEntity<?> getRolesForOrganisation(@PathVariable Long id, Pageable pageable)
    {
        return organisationRolesService.getRolesForOrganisation(id,pageable);
    }

    @GetMapping("/check-for-deletion/{id}")
    public ResponseEntity<?> checkForDeletion(@PathVariable Long id)
    {
        return organisationRolesService.checkForDeletion(id);
    }

    @GetMapping("/organisation-list/{id}")
    public ResponseEntity<?> getRolesForOrganisationList(@PathVariable Long id)
    {
        return organisationRolesService.getRolesForOrganisation(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrganisationRole role)
    {
        return organisationRolesService.save(role);
    }

    @PostMapping("/save-for-user/{id}")
    public ResponseEntity<?> save(@PathVariable Long id,@RequestBody List<OrganisationRole> roles)
    {
        return organisationRolesService.saveForUser(id,roles);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
        return organisationRolesService.delete(id);
    }
}
