package pl.kantoch.dawid.magit.services;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.Organisation;
import pl.kantoch.dawid.magit.models.Project;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.models.Team;
import pl.kantoch.dawid.magit.repositories.OrganisationsRepository;
import pl.kantoch.dawid.magit.repositories.ProjectsRepository;
import pl.kantoch.dawid.magit.repositories.TasksRepository;
import pl.kantoch.dawid.magit.repositories.TeamsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TasksService
{
    private final Logger LOGGER = LoggerFactory.getLogger(TasksService.class);

    private final TasksRepository tasksRepository;
    private final TeamsRepository teamsRepository;
    private final ProjectsRepository projectsRepository;
    private final OrganisationsRepository organisationsRepository;

    private final Gson gson = new Gson();

    public TasksService(TasksRepository tasksRepository,
                        TeamsRepository teamsRepository,
                        ProjectsRepository projectsRepository,
                        OrganisationsRepository organisationsRepository) {
        this.tasksRepository = tasksRepository;
        this.teamsRepository = teamsRepository;
        this.projectsRepository = projectsRepository;
        this.organisationsRepository = organisationsRepository;
    }

    @Transactional
    public ResponseEntity<?> save(Task task){
        try {
            if(task.getId()==null){
                task.setCreationDate(new Date());
                task.setDeleted(false);
            } else task.setModificationDate(new Date());
            Task saved = tasksRepository.save(task);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(saved);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.save for entity {}. Message: {}",task,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas zapisywania zadania. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForProjectNoPage(Long id){
        try {
            Optional<Project> optionalProject = projectsRepository.findByIdAndDeletedIsFalseAndArchivedIsFalse(id);
            if(optionalProject.isEmpty())
                throw new Exception("Nie znaleziono projektu o ID="+id);
            List<Task> list = tasksRepository.findAllByDeletedFalseAndProject(optionalProject.get());
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForProject for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForProjectPageable(Long id, Pageable pageable){
        try {
            Optional<Project> optionalProject = projectsRepository.findByIdAndDeletedIsFalseAndArchivedIsFalse(id);
            if(optionalProject.isEmpty())
                throw new Exception("Nie znaleziono projektu o ID="+id);
            Page<Task> page = tasksRepository.findAllByDeletedFalseAndProject(optionalProject.get(),pageable);
            return ResponseEntity.ok().body(page);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForProjectPageable for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForTeamNoPage(Long id){
        try {
            Optional<Team> optionalTeam = teamsRepository.findByDeletedFalseAndId(id);
            if(optionalTeam.isEmpty())
                throw new Exception("Nie znaleziono zespołu o ID="+id);
            List<Task> list = tasksRepository.findAllByDeletedFalseAndTeam(optionalTeam.get());
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForTeamNoPage for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForTeamPageable(Long id, Pageable pageable){
        try {
            Optional<Team> optionalTeam = teamsRepository.findByDeletedFalseAndId(id);
            if(optionalTeam.isEmpty())
                throw new Exception("Nie znaleziono zespołu o ID="+id);
            Page<Task> list = tasksRepository.findAllByDeletedFalseAndTeam(optionalTeam.get(),pageable);
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForTeamPageable for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForOrganisationNoPage(Long id){
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty())
                throw new Exception("Nie znaleziono organizacji o ID="+id);
            List<Task> list = tasksRepository.findAllByDeletedFalseAndOrganisation(optionalOrganisation.get());
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForOrganisationNoPage for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForOrganisationPageable(Long id, Pageable pageable){
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty())
                throw new Exception("Nie znaleziono organizacji o ID="+id);
            Page<Task> page = tasksRepository.findAllByDeletedFalseAndOrganisation(optionalOrganisation.get(),pageable);
            return ResponseEntity.ok().body(page);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForOrganisationPageable for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> delete(Long id){
        try {
            Optional<Task> optionalTask = tasksRepository.findById(id);
            if(optionalTask.isEmpty())
                throw new Exception("Nie znaleziono zadania o ID="+id);
            tasksRepository.setAsDeleted(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.delete for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Błąd podczas usuwania zadania. "+e.getMessage()));
        }
    }
}
