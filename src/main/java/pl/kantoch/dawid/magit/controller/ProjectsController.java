package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.Project;
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

    @GetMapping("/get-all/{id}")
    public ResponseEntity<?> getAllProjectsForOrg(@PathVariable Long id)
    {
        return projectsService.getAllProjectsForOrg(id);
    }

    @GetMapping("/archive/{id}")
    public ResponseEntity<?> archiveProject(@PathVariable Long id)
    {
        return projectsService.archiveProject(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id)
    {
        return projectsService.deleteProject(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Project project)
    {
        return projectsService.save(project);
    }
}
