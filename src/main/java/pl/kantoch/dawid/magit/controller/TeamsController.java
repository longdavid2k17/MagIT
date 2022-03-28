package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<?> getAllTeamsInOrganisation(@PathVariable Long id)
    {
        return teamsService.getAllTeamsInOrganisation(id);
    }
}
