package pl.kantoch.dawid.magit.services;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.repositories.ProjectsRepository;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectsService
{
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectsService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectsRepository projectsRepository;

    private final Gson gson = new Gson();

    public ProjectsService(UserRepository userRepository,
                           RoleRepository roleRepository,
                           ProjectsRepository projectsRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.projectsRepository = projectsRepository;
    }

    public ResponseEntity<?> getAllPMsForOrg(Long id)
    {
        try
        {
            Optional<Role> optional = roleRepository.findByName(ERole.ROLE_PM);
            if(optional.isPresent())
            {
                List<User> allPmUsers = userRepository.findAllByOrganisation_IdAndRolesContains(id,optional.get());
                return ResponseEntity.ok().body(allPmUsers);
            }
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Brak użytkowników z rolą PM w twojej organizacji"));
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.getAllPMsForOrg for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania użytkowników z rolą PM. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> save(Project project)
    {
        try
        {
            if(project.getArchived()==null)
                project.setArchived(false);
            if(project.getDeleted()==null)
                project.setDeleted(false);
            if(project.getId()!=null)
                project.setModificationDate(new Date());
            if(project.getId()==null)
            {
                project.setCreateDate(new Date());
                project.setModificationDate(new Date());
            }
            Project saved = projectsRepository.save(project);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.save for entity {}. Message: {}",project,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas zapisu projektu! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllProjectsForOrg(Long id)
    {
        try
        {
            List<Project> projects = projectsRepository.findAllByOrganisation_IdAndDeletedFalse(id);
            projects.forEach(e->{
                e.setAllTasks("79/112");
                e.setTodayTasks("2/5");
            });
            return ResponseEntity.ok().body(projects);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.getAllProjectsForOrg for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania projektów. Komunikat: "+e.getMessage()));
        }
    }
}