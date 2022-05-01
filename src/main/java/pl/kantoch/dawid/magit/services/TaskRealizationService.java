package pl.kantoch.dawid.magit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.models.TaskRealization;
import pl.kantoch.dawid.magit.models.TeamMember;
import pl.kantoch.dawid.magit.repositories.TaskRealizationRepository;
import pl.kantoch.dawid.magit.repositories.TasksRepository;
import pl.kantoch.dawid.magit.repositories.TeamMembersRepository;
import pl.kantoch.dawid.magit.security.JWTUtils;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.Date;
import java.util.Optional;

@Service
public class TaskRealizationService
{
    private final Logger LOGGER = LoggerFactory.getLogger(TaskRealizationService.class);

    private final TaskRealizationRepository taskRealizationRepository;
    private final TasksRepository tasksRepository;
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final TeamMembersRepository teamMembersRepository;

    public TaskRealizationService(TaskRealizationRepository taskRealizationRepository, TasksRepository tasksRepository, UserRepository userRepository, JWTUtils jwtUtils, TeamMembersRepository teamMembersRepository) {
        this.taskRealizationRepository = taskRealizationRepository;
        this.tasksRepository = tasksRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.teamMembersRepository = teamMembersRepository;
    }

    public ResponseEntity<?> verifyRealizationPossibility(Long taskId,String token){
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalseAndCompletedFalse(taskId);
            if(optionalTask.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono zadania o ID="+taskId));
            String username = jwtUtils.getUsernameFromJwtToken(token.substring(7));
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika!"));
            Task task = optionalTask.get();
            if(task.getUser()!=null && !task.getUser().getId().equals(optionalUser.get().getId())){
                if(task.getTeam()!=null){
                    Optional<TeamMember> optionalTeamMember = teamMembersRepository.findByUser_IdAndTeam_Id(optionalUser.get().getId(),task.getTeam().getId());
                    if(optionalTeamMember.isEmpty()) return ResponseEntity.ok().body(false);
                } else return ResponseEntity.ok().body(false);
            }
            return ResponseEntity.ok().body(true);
        }
        catch (Exception e){
            LOGGER.error("Error in TaskRealizationService.verifyRealizationPossibility for taskId={}. Message: {}",taskId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas weryfikacji zadania dla użytkownika! Komunikat: "+e.getMessage()));
        }
    }

    public ResponseEntity<?> getRealizationStatus(Long taskId,String token){
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalseAndCompletedFalse(taskId);
            if(optionalTask.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono zadania o ID="+taskId));
            String username = jwtUtils.getUsernameFromJwtToken(token.substring(7));
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika!"));
            Task task = optionalTask.get();
            if(task.getUser()!=null && !task.getUser().getId().equals(optionalUser.get().getId())){
                if(task.getTeam()!=null){
                    Optional<TeamMember> optionalTeamMember = teamMembersRepository.findByUser_IdAndTeam_Id(optionalUser.get().getId(),task.getTeam().getId());
                    if(optionalTeamMember.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Użytkownik nie ma prawa do uruchomienia realizacji tego zadania!"));
                } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Użytkownik nie ma prawa do uruchomienia realizacji tego zadania!"));
            }

            Optional<TaskRealization> optionalTaskRealization = taskRealizationRepository.findAllByTaskAndUser_IdAndStatusEquals(task,optionalUser.get().getId(),"REALIZACJA");
            if(optionalTaskRealization.isEmpty()) return ResponseEntity.ok().body(GsonInstance.get().toJson("Brak realizacji"));
            else return ResponseEntity.ok().body(GsonInstance.get().toJson("Realizacja w trakcie"));
        }
        catch (Exception e){
            LOGGER.error("Error in TaskRealizationService.getRealizationStatus for taskId={}. Message: {}",taskId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas weryfikacji stanu realizacji zadania! Komunikat: "+e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> changeStatus(Long taskId,String token){
        try {
            Optional<Task> optionalTask = tasksRepository.findByIdAndDeletedFalseAndCompletedFalse(taskId);
            if(optionalTask.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono zadania o ID="+taskId));
            String username = jwtUtils.getUsernameFromJwtToken(token.substring(7));
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if(optionalUser.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Nie znaleziono użytkownika!"));
            Task task = optionalTask.get();
            if(task.getUser()!=null && !task.getUser().getId().equals(optionalUser.get().getId())){
                if(task.getTeam()!=null){
                    Optional<TeamMember> optionalTeamMember = teamMembersRepository.findByUser_IdAndTeam_Id(optionalUser.get().getId(),task.getTeam().getId());
                    if(optionalTeamMember.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Użytkownik nie ma prawa do uruchomienia realizacji tego zadania!"));
                } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Użytkownik nie ma prawa do uruchomienia realizacji tego zadania!"));
            }

            Optional<TaskRealization> optionalTaskRealization = taskRealizationRepository.findAllByTaskAndUser_IdAndStatusEquals(task,optionalUser.get().getId(),"REALIZACJA");
            if(optionalTaskRealization.isPresent()){
                TaskRealization taskRealization = optionalTaskRealization.get();
                taskRealization.setEndDate(new Date());
                taskRealization.setStatus("KONIEC");
                taskRealizationRepository.save(taskRealization);
            } else {
              TaskRealization taskRealization = new TaskRealization();
              taskRealization.setTask(task);
              taskRealization.setUser(optionalUser.get());
              taskRealization.setStartDate(new Date());
              taskRealization.setStatus("REALIZACJA");
              taskRealizationRepository.save(taskRealization);
            }
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("Error in TaskRealizationService.changeStatus for taskId={}. Message: {}",taskId,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Wystąpił błąd podczas zmiany statusu realizacji zadania! Komunikat: "+e.getMessage()));
        }
    }
}
