package pl.kantoch.dawid.magit.services;

import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.*;
import pl.kantoch.dawid.magit.models.payloads.responses.DailyTasks;
import pl.kantoch.dawid.magit.models.payloads.responses.MyTasksWrapper;
import pl.kantoch.dawid.magit.models.payloads.responses.TaskWrapper;
import pl.kantoch.dawid.magit.repositories.*;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.DateUtils;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TasksService
{
    private final Logger LOGGER = LoggerFactory.getLogger(TasksService.class);

    private final TasksRepository tasksRepository;
    private final TeamsRepository teamsRepository;
    private final ProjectsRepository projectsRepository;
    private final OrganisationsRepository organisationsRepository;
    private final ExampleSolutionsRepository exampleSolutionsRepository;
    private final UserRepository userRepository;
    private final TaskRealizationRepository taskRealizationRepository;

    public TasksService(TasksRepository tasksRepository,
                        TeamsRepository teamsRepository,
                        ProjectsRepository projectsRepository,
                        OrganisationsRepository organisationsRepository,
                        ExampleSolutionsRepository exampleSolutionsRepository,
                        UserRepository userRepository,
                        TaskRealizationRepository taskRealizationRepository) {
        this.tasksRepository = tasksRepository;
        this.teamsRepository = teamsRepository;
        this.projectsRepository = projectsRepository;
        this.organisationsRepository = organisationsRepository;
        this.exampleSolutionsRepository = exampleSolutionsRepository;
        this.userRepository = userRepository;
        this.taskRealizationRepository = taskRealizationRepository;
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
            }
            else task.setModificationDate(new Date());
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

    public ResponseEntity<?> getForProjectPageable(Long id, Pageable pageable,String query){
        try {
            Optional<Project> optionalProject = projectsRepository.findByIdAndDeletedIsFalseAndArchivedIsFalse(id);
            if(optionalProject.isEmpty())
                throw new Exception("Nie znaleziono projektu o ID="+id);
            Page<Task> page;
            if(query!=null) {
                query = "%"+query.toLowerCase(Locale.ROOT)+"%";
                page = tasksRepository.findAllByDeletedFalseAndProjectAndParentTaskIsNullFitlered(optionalProject.get(),query,pageable);
            } else page = tasksRepository.findAllByDeletedFalseAndProjectAndParentTaskIsNull(optionalProject.get(),pageable);
            page.get().forEach(e->{
                Optional<ExampleSolutions> optional = exampleSolutionsRepository.findByTask(e);
                if(optional.isPresent()) e.setExample(true);
            });
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

    public ResponseEntity<?> getForTeamPageable(Long id, Pageable pageable,String query){
        try {
            Optional<Team> optionalTeam = teamsRepository.findByDeletedFalseAndId(id);
            if(optionalTeam.isEmpty())
                throw new Exception("Nie znaleziono zespołu o ID="+id);
            Page<Task> page;
            if(query!=null) {
                query = "%"+query.toLowerCase(Locale.ROOT)+"%";
                page = tasksRepository.findAllByDeletedFalseAndTeamAndParentTaskIsNullFiltered(optionalTeam.get(),query,pageable);
            } else page = tasksRepository.findAllByDeletedFalseAndTeamAndParentTaskIsNull(optionalTeam.get(),pageable);
            page.get().forEach(e->{
                Optional<ExampleSolutions> optional = exampleSolutionsRepository.findByTask(e);
                if(optional.isPresent()) e.setExample(true);
            });
            return ResponseEntity.ok().body(page);
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

    public ResponseEntity<?> getForOrganisationPageable(Long id, Pageable pageable,String query){
        try {
            Optional<Organisation> optionalOrganisation = organisationsRepository.findById(id);
            if(optionalOrganisation.isEmpty())
                throw new Exception("Nie znaleziono organizacji o ID="+id);
            Page<Task> page;
            if(query!=null) {
                query = "%"+query.toLowerCase(Locale.ROOT)+"%";
                page = tasksRepository.findAllByDeletedFalseAndOrganisationAndParentTaskIsNullFiltered(optionalOrganisation.get(),query,pageable);
            }
            else page = tasksRepository.findAllByDeletedFalseAndOrganisationAndParentTaskIsNull(optionalOrganisation.get(),pageable);
            page.get().forEach(e->{
                Optional<ExampleSolutions> optional = exampleSolutionsRepository.findByTask(e);
                if(optional.isPresent()) e.setExample(true);
            });
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

    @Transactional
    public ResponseEntity<?> editSubtasks(List<Task> tasks,Long parentTaskId)
    {
        List<Task> list = tasksRepository.findAllByDeletedFalseAndParentTask_Id(parentTaskId);
        tasksRepository.deleteAll(list);
        try {
            tasks.forEach(e->{
                if(e.getId()==null){
                    e.setDeleted(false);
                    e.setCompleted(false);
                    e.setDeadlineDate(e.getParentTask().getDeadlineDate());
                    e.setStartDate(e.getParentTask().getStartDate());
                    e.setTeam(e.getParentTask().getTeam());
                    e.setProject(e.getParentTask().getProject());
                    e.setGitHubUrl(e.getParentTask().getGitHubUrl());
                    e.setStatus(e.getParentTask().getStatus());
                    e.setCreationDate(new Date());
                }
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

    @Transactional
    public ResponseEntity<?> setTaskStatusAsComplete(Long id){
        try {
            Task task = findByIdAndDeletedFalse(id);
            if(task==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono zadania o ID="+id));
            task.setStatus("WYKONANE");
            task.setModificationDate(new Date());
            task.setCompleted(true);
            tasksRepository.save(task);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.setTaskStatusAsComplete for ID {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zmiany statusu zadania. Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> setTaskStatusAsInRealization(Long id){
        try {
            Task task = findByIdAndDeletedFalse(id);
            if(task==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono zadania o ID="+id));
            task.setStatus("REALIZACJA");
            task.setCompleted(false);
            task.setModificationDate(new Date());
            tasksRepository.save(task);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.setTaskStatusAsInRealization for ID {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas zmiany statusu zadania. Komunikat: "+e.getMessage()));
        }
    }

    public Task findByIdAndDeletedFalse(Long id){
        Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalse(id);
        if(optionalTask.isEmpty()) return null;
        else return optionalTask.get();
    }


    public ResponseEntity<?> getTaskWrapper(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if(user.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika o ID="+id));
            TaskWrapper taskWrapper = new TaskWrapper();
            Optional<TaskRealization> taskRealizationOptional = taskRealizationRepository.findByUserAndStatusEquals(user.get(),"REALIZACJA");
            taskRealizationOptional.ifPresent(taskRealization -> taskWrapper.setTask(taskRealization.getTask()));
            LocalDateTime morningDate = LocalDate.now().atStartOfDay();
            LocalDateTime eveningDate = LocalTime.MAX.atDate(LocalDate.now());
            Date dateMorningConverted =  DateUtils.convertToDateViaSqlTimestamp(morningDate);
            Date dateEveningConverted =  DateUtils.convertToDateViaSqlTimestamp(eveningDate);
            Long countUserDoneToday = tasksRepository.countAllByDeletedFalseAndUserAndCompletedTrueAndModificationDateBetween(user.get(),dateMorningConverted,dateEveningConverted);
            Long countUserAllToday = tasksRepository.countAllByDeletedFalseAndUserAndModificationDateBetween(user.get(),dateMorningConverted,dateEveningConverted);
            taskWrapper.setTodayUserTasks("Wykonane dzisiaj: "+countUserDoneToday+"/"+countUserAllToday+" zadań");
            List<Team> teams = teamsRepository.findForUser(user.get().getId());
            AtomicReference<Long> allUserTeamsDoneToday = new AtomicReference<>(0L);
            AtomicReference<Long> allUserTeamsAllToday = new AtomicReference<>(0L);
            teams.forEach(e->{
                allUserTeamsDoneToday.updateAndGet(v -> v + tasksRepository.countAllByDeletedFalseAndTeamAndCompletedTrueAndModificationDateBetween(e, dateMorningConverted, dateEveningConverted));
                allUserTeamsAllToday.updateAndGet(v -> v + tasksRepository.countAllByDeletedFalseAndTeamAndModificationDateBetween(e, dateMorningConverted, dateEveningConverted));
            });
            taskWrapper.setTodayTeamTasks("Zadania twoich zespołów: "+allUserTeamsDoneToday.get()+"/"+allUserTeamsAllToday.get());
            return ResponseEntity.ok().body(taskWrapper);
        }
        catch (Exception e){
            LOGGER.error("Error in TasksService.getTaskWrapper for ID {}. Message: {}",id,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania asystenta zadań. Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getMyTasksWrapper(Long id,String mode) {
        if(mode==null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak parametru trybu wyboru!"));
        try {
            MyTasksWrapper wrapper = new MyTasksWrapper();
            switch (mode){
                case "day":
                    wrapper.setList(getForDay(id));
                    break;
                case "week":
                    wrapper.setList(getForWeek(id));
                    break;
                case "month":
                    wrapper.setList(getForMonth(id));
                    break;
                default:
                    break;
            }
            return ResponseEntity.ok().body(wrapper);
        }
        catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Error in TasksService.getMyTasksWrapper for ID {} and mode param = {}. Message: {}",id,mode,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Błąd podczas pobierania zadań. Komunikat: "+e.getMessage()));
        }
    }

    private List<DailyTasks> getForDay(Long userId){
        DailyTasks dailyTasks = getDailyTasks(userId,LocalDate.now());
        return List.of(dailyTasks);
    }

    private List<DailyTasks> getForWeek(Long userId){
        LocalDate monday = LocalDate.now().withDayOfMonth(DateTimeConstants.MONDAY);
        LocalDate sunday = LocalDate.now().withDayOfMonth(DateTimeConstants.SUNDAY);
        List<DailyTasks> list = new ArrayList<>();
        for (LocalDate d = monday; !d.isAfter(sunday); d = d.plusDays(1)) {
            list.add(getDailyTasks(userId,d));
        }
        return list;
    }

    private List<DailyTasks> getForMonth(Long userId){
        LocalDate firstDayOfMonth = LocalDateTime.now()
                .with(LocalTime.MIN)
                .with(TemporalAdjusters.firstDayOfMonth()).toLocalDate();
        LocalDate lastDayOfMonth = LocalDateTime.now()
                .with(LocalTime.MIN)
                .with(TemporalAdjusters.lastDayOfMonth()).toLocalDate();
        List<DailyTasks> list = new ArrayList<>();
        for (LocalDate d = firstDayOfMonth; !d.isAfter(lastDayOfMonth); d = d.plusDays(1)) {
            list.add(getDailyTasks(userId,d));
        }
        return list;
    }

    public DailyTasks getDailyTasks(Long userId,LocalDate localDate){
        LocalDateTime morningDate = localDate.atStartOfDay();
        LocalDateTime eveningDate = LocalTime.MAX.atDate(localDate);
        Date dateMorningConverted =  DateUtils.convertToDateViaSqlTimestamp(morningDate);
        Date dateEveningConverted =  DateUtils.convertToDateViaSqlTimestamp(eveningDate);
        String label = getDayOfWeek(localDate)+", "+localDate;
        List<Task> tasks = tasksRepository.getAllForDay(userId,dateMorningConverted,dateEveningConverted);
        return new DailyTasks(label,tasks);
    }

    public String getDayOfWeek(LocalDate localDate){
        String day = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL,Locale.ENGLISH);
        String translated;
        switch (day.toLowerCase(Locale.ROOT)){
            case "monday":
            default:
                translated="Poniedziałek";
                break;
            case "tuesday":
                translated="Wtorek";
                break;
            case "wednesday":
                translated="Środa";
                break;
            case "thursday":
                translated="Czwartek";
                break;
            case "friday":
                translated="Piątek";
                break;
            case "saturday":
                translated="Sobota";
                break;
            case "sunday":
                translated="Niedziela";
                break;
        }
        return translated;
    }
}
