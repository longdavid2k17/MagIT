package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.OrganisationRole;
import pl.kantoch.dawid.magit.services.OrganisationRolesService;

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
    public ResponseEntity<?> getRolesForOrganisation(@PathVariable Long id)
    {
        return organisationRolesService.getRolesForOrganisation(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrganisationRole role)
    {
        return organisationRolesService.save(role);
    }
}
