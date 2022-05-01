package pl.kantoch.dawid.magit.services;

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
import pl.kantoch.dawid.magit.utils.GsonInstance;

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
            if(task.getDeadlineDate()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak zdefiniowanej daty zakończenia!"));
            if(task.getTeam()==null && task.getUser()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak zdefiniowanego osoby/zespołu odpowiedzialnego za realizację!"));
            if(task.getOrganisation()==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak danych organizacji w żądaniu!"));
            if(task.getId()==null){
                task.setCreationDate(new Date());
                task.setDeleted(false);
                task.setStatus("NOWY");
            } else task.setModificationDate(new Date());
            if(task.getStartDate()!=null){
                if(task.getStartTime()!=null){
                    String[] parts = task.getStartTime().split(":");
                    int hour = Integer.parseInt(parts[0]);
                    int minutes = Integer.parseInt(parts[1]);
                    Date startTime = task.getStartDate();
                    startTime.setHours(hour);
                    startTime.setMinutes(minutes);
                    task.setStartDate(startTime);
                }
            } else task.setStartDate(new Date());
            if(task.getDeadlineTime()!=null){
                String[] parts = task.getDeadlineTime().split(":");
                int hour = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                Date endTime = task.getDeadlineDate();
                endTime.setHours(hour);
                endTime.setMinutes(minutes);
                task.setDeadlineDate(endTime);
            }
            Task saved = tasksRepository.save(task);
            return ResponseEntity.ok().body(saved);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.save for entity {}. Message: {}",task,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zapisywania zadania. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForProjectNoPage(Long id){
        try {
            Optional<Project> optionalProject = projectsRepository.findByIdAndDeletedIsFalseAndArchivedIsFalse(id);
            if(optionalProject.isEmpty())
                throw new Exception("Nie znaleziono projektu o ID="+id);
            List<Task> list = tasksRepository.findAllByDeletedFalseAndProjectAndParentTaskIsNull(optionalProject.get());
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForProject for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForProjectPageable(Long id, Pageable pageable){
        try {
            Optional<Project> optionalProject = projectsRepository.findByIdAndDeletedIsFalseAndArchivedIsFalse(id);
            if(optionalProject.isEmpty())
                throw new Exception("Nie znaleziono projektu o ID="+id);
            Page<Task> page = tasksRepository.findAllByDeletedFalseAndProjectAndParentTaskIsNull(optionalProject.get(),pageable);
            return ResponseEntity.ok().body(page);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForProjectPageable for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForTeamNoPage(Long id){
        try {
            Optional<Team> optionalTeam = teamsRepository.findByDeletedFalseAndId(id);
            if(optionalTeam.isEmpty())
                throw new Exception("Nie znaleziono zespołu o ID="+id);
            List<Task> list = tasksRepository.findAllByDeletedFalseAndTeamAndParentTaskIsNull(optionalTeam.get());
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForTeamNoPage for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForTeamPageable(Long id, Pageable pageable){
        try {
            Optional<Team> optionalTeam = teamsRepository.findByDeletedFalseAndId(id);
            if(optionalTeam.isEmpty())
                throw new Exception("Nie znaleziono zespołu o ID="+id);
            Page<Task> list = tasksRepository.findAllByDeletedFalseAndTeamAndParentTaskIsNull(optionalTeam.get(),pageable);
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForTeamPageable for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForOrganisationNoPage(Long id){
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty())
                throw new Exception("Nie znaleziono organizacji o ID="+id);
            List<Task> list = tasksRepository.findAllByDeletedFalseAndOrganisationAndParentTaskIsNull(optionalOrganisation.get());
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForOrganisationNoPage for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getForOrganisationPageable(Long id, Pageable pageable){
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty())
                throw new Exception("Nie znaleziono organizacji o ID="+id);
            Page<Task> page = tasksRepository.findAllByDeletedFalseAndOrganisationAndParentTaskIsNull(optionalOrganisation.get(),pageable);
            return ResponseEntity.ok().body(page);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getForOrganisationPageable for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania listy zadań. "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> delete(Long id){
        try {
            Optional<Task> optionalTask = tasksRepository.findById(id);
            if(optionalTask.isEmpty())
                throw new Exception("Nie znaleziono zadania o ID="+id);
            Task task = optionalTask.get();
            task.setDeleted(true);
            task.setStatus("USUNIĘTO");
            task.setModificationDate(new Date());
            tasksRepository.save(task);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.delete for id {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas usuwania zadania. "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> saveSubtasks(List<Task> tasks)
    {
        try {
            tasks.forEach(e->{
                e.setDeleted(false);
                e.setCompleted(false);
                e.setDeadlineDate(e.getParentTask().getDeadlineDate());
                e.setStartDate(e.getParentTask().getStartDate());
                e.setTeam(e.getParentTask().getTeam());
                e.setProject(e.getParentTask().getProject());
                e.setGitHubUrl(e.getParentTask().getGitHubUrl());
                e.setStatus(e.getParentTask().getStatus());
                e.setCreationDate(new Date());
            });
            tasksRepository.saveAll(tasks);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.saveSubtasks for entities {}. Message: {}",tasks,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zapisywania podzadań. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getSubtasks(Long id)
    {
        try {
            List<Task> list = tasksRepository.findAllByDeletedFalseAndParentTask_Id(id);
            return ResponseEntity.ok().body(list);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getSubtasks for ID {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania podzadań. Komunikat: "+e.getMessage()));
        }
    }
}
