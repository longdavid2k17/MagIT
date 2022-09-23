package pl.kantoch.dawid.magit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.services.TaskRealizationService;

@RestController
@RequestMapping("/api/realization")
public class TaskRealizationController
{
    private final TaskRealizationService taskRealizationService;

    public TaskRealizationController(TaskRealizationService taskRealizationService) {
        this.taskRealizationService = taskRealizationService;
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity<?> verifyRealizationPossibility(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return taskRealizationService.verifyRealizationPossibility(id,token);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getRealizationStatus(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return taskRealizationService.getRealizationStatus(id,token);
    }

    @GetMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestHeader("Authorization") String token){
        return taskRealizationService.changeStatus(id,token);
    }
}
