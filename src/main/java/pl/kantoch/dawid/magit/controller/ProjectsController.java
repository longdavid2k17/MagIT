package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kantoch.dawid.magit.services.ProjectsService;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController
{
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping("/get-all-pms/{id}")
    public ResponseEntity<?> getAllPMUsersForOrg(@PathVariable Long id)
    {
        return projectsService.getAllPMsForOrg(id);
    }
}
