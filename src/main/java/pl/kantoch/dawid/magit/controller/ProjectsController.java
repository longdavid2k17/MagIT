package pl.kantoch.dawid.magit.controller;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.services.ProjectsService;

import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectsController
{
    private final ProjectsService projectsService;

    private final Gson gson = new Gson();

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping("/get-all-pms/{id}")
    public ResponseEntity<?> getAllPMUsersForOrg(@PathVariable Long id)
    {
        return projectsService.getAllPMsForOrg(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllProjectsForOrg(@RequestParam MultiValueMap<String, String> params)
    {
        String searchedString = Optional.ofNullable(params.getFirst("search")).orElse(null);
        String idStr = Optional.ofNullable(params.getFirst("id")).orElse(null);
        if(idStr==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Brak parametru ID w zapytaniu!"));
        Long id = Long.valueOf(idStr);
        if(searchedString!=null)
        {
            return projectsService.getFilteredProjectsForOrg(id,searchedString);
        }
        else return projectsService.getAllProjectsForOrg(id);
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
