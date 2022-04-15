package pl.kantoch.dawid.magit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.payloads.requests.CreateTeamRequest;
import pl.kantoch.dawid.magit.services.TeamsService;

@RestController
@RequestMapping("/api/teams")
public class TeamsController
{
    private final TeamsService teamsService;

    public TeamsController(TeamsService teamsService)
    {
        this.teamsService = teamsService;
    }

    @GetMapping("/organisation/{id}")
    public ResponseEntity<?> getAllTeamsInOrganisation(@PathVariable Long id, Pageable pageable)
    {
        return teamsService.getAllTeamsInOrganisation(id,pageable);
    }

    @GetMapping("/organisation-nopage/{id}")
    public ResponseEntity<?> getAllTeamsInOrganisationNoPage(@PathVariable Long id)
    {
        return teamsService.getAllTeamsInOrganisation(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> createNewTeam(@RequestBody CreateTeamRequest request)
    {
        return teamsService.save(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id){
        return teamsService.deleteTeam(id);
    }
}
