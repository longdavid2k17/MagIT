package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.repositories.ProjectsRepository;
import pl.kantoch.dawid.magit.repositories.TasksRepository;
import pl.kantoch.dawid.magit.security.user.ERole;
import pl.kantoch.dawid.magit.security.user.Role;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.RoleRepository;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.DateUtils;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProjectsService
{
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectsService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectsRepository projectsRepository;
    private final TasksRepository tasksRepository;
    private final MemoryDirectoriesService memoryDirectoriesService;

    public ProjectsService(UserRepository userRepository,
                           RoleRepository roleRepository,
                           ProjectsRepository projectsRepository,
                           TasksRepository tasksRepository,
                           MemoryDirectoriesService memoryDirectoriesService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.projectsRepository = projectsRepository;
        this.tasksRepository = tasksRepository;
        this.memoryDirectoriesService = memoryDirectoriesService;
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
            else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak użytkowników z rolą PM w twojej organizacji"));
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.getAllPMsForOrg for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania użytkowników z rolą PM. Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> save(Project project)
    {
        boolean creation = false;
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
                creation=true;
                project.setCreateDate(new Date());
                project.setModificationDate(new Date());
            }
            Project saved = projectsRepository.save(project);
            if(creation && saved.getDriveName()!=null)
            {
                if(!memoryDirectoriesService.createNAS(saved))
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił problem podczas tworzenia przestrzeni dyskowej i nie została ona utworzona!"));
            }
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.save for entity {}. Message: {}",project,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zapisu projektu! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllProjectsForOrg(Long id)
    {
        try
        {
            List<Project> projects = projectsRepository.findAllByOrganisation_IdAndDeletedFalse(id);
            projects.forEach(e->{
                long completedTasks = tasksRepository.countAllByCompletedTrueAndDeletedFalseAndProject(e);
                long allTasks = tasksRepository.countAllByDeletedFalseAndProject(e);
                LocalDateTime morningDate = LocalDate.now().atStartOfDay();
                LocalDateTime eveningDate = LocalTime.MAX.atDate(LocalDate.now());
                long completedTasksToday = tasksRepository.countTodayCompletedTasks(e, DateUtils.convertToDateViaSqlTimestamp(morningDate),DateUtils.convertToDateViaSqlTimestamp(eveningDate));
                e.setAllTasks(completedTasks+"/"+allTasks);
                e.setTodayTasks(String.valueOf(completedTasksToday));
            });
            return ResponseEntity.ok().body(projects);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.getAllProjectsForOrg for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania projektów. Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> archiveProject(Long id) {
        try
        {
            Optional<Project> optionalProject = projectsRepository.findById(id);
            if(optionalProject.isPresent())
            {
                Project project = optionalProject.get();
                project.setArchived(true);
                projectsRepository.save(project);
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.archiveProject for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas archiwizowania projektu o ID="+id+". Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProject(Long id) {
        try
        {
            Optional<Project> optionalProject = projectsRepository.findById(id);
            if(optionalProject.isPresent())
            {
                Project project = optionalProject.get();
                project.setDeleted(true);
                projectsRepository.save(project);
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.delete for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas usuwania projektu o ID="+id+". Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getFilteredProjectsForOrg(Long id, String searchedString)
    {
        try
        {
            String keyword = "%"+searchedString.toLowerCase(Locale.ROOT)+"%";
            List<Project> projects = projectsRepository.findBySearchKeyword(id,keyword);
            projects.forEach(e->{
                long completedTasks = tasksRepository.countAllByCompletedTrueAndDeletedFalseAndProject(e);
                long allTasks = tasksRepository.countAllByDeletedFalseAndProject(e);
                LocalDateTime morningDate = LocalDate.now().atStartOfDay();
                LocalDateTime eveningDate = LocalTime.MAX.atDate(LocalDate.now());
                long completedTasksToday = tasksRepository.countTodayCompletedTasks(e, DateUtils.convertToDateViaSqlTimestamp(morningDate),DateUtils.convertToDateViaSqlTimestamp(eveningDate));
                e.setAllTasks(completedTasks+"/"+allTasks);
                e.setTodayTasks(String.valueOf(completedTasksToday));
            });
            return ResponseEntity.ok().body(projects);
        }
        catch (Exception e)
        {
            LOGGER.error("Error in ProjectsService.getAllProjectsForOrg for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania projektów. Komunikat: "+e.getMessage()));
        }
    }
}
