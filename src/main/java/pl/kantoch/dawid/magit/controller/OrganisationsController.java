package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.services.OrganisationsService;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationsController
{
    private final OrganisationsService organisationsService;

    public OrganisationsController(OrganisationsService organisationsService)
    {
        this.organisationsService = organisationsService;
    }

    @GetMapping("/owner-id/{id}")
    public ResponseEntity<?> getOrganisationByOwnerId(@PathVariable Long id)
    {
        return organisationsService.getOrganisationByOwnerId(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Organisation organisation)
    {
        return organisationsService.save(organisation);
    }
}
