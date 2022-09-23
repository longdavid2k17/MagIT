package pl.kantoch.dawid.magit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.payloads.requests.CreateTeamRequest;
import pl.kantoch.dawid.magit.models.payloads.requests.EditTeamRequest;
import pl.kantoch.dawid.magit.services.TeamsService;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin("*")
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

    @GetMapping("/all-user-teams-nopage/{id}")
    public ResponseEntity<?> getAllUserTeamsNoPage(@PathVariable Long id)
    {
        return teamsService.getAllUserTeamsNoPage(id);
    }

    @GetMapping("/teammembers-nopage/{id}")
    public ResponseEntity<?> getAllTeamMembersNoPage(@PathVariable Long id)
    {
        return teamsService.getAllTeamMembersNoPage(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> createNewTeam(@RequestBody CreateTeamRequest request)
    {
        return teamsService.save(request);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editTeam(@RequestBody EditTeamRequest request)
    {
        return teamsService.edit(request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id){
        return teamsService.deleteTeam(id);
    }
}
