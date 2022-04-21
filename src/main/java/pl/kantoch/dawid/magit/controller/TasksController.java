package pl.kantoch.dawid.magit.controller;

import com.google.gson.Gson;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.services.TasksService;

@RestController
@RequestMapping("/api/tasks")
public class TasksController
{
    private final TasksService tasksService;

    private final Gson gson = new Gson();

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Task task){
        return tasksService.save(task);
    }

    @GetMapping("/{selectionMode}/{id}")
    public ResponseEntity<?> getTasksNoPage(@PathVariable String selectionMode,@PathVariable Long id){
        if(selectionMode==null || id==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Brak wymaganych parametrów do pobrania zasobów!"));
        ResponseEntity<?> response;
        switch (selectionMode){
            case "project":
                response = tasksService.getForProjectNoPage(id);
                break;
            case "team":
                response = tasksService.getForTeamNoPage(id);
                break;
            default:
                response = ResponseEntity.ok().build();
                break;
        }
        return response;
    }

    @GetMapping("/pageable/{selectionMode}/{id}")
    public ResponseEntity<?> getTasksPageable(@PathVariable String selectionMode, @PathVariable Long id, Pageable pageable){
        if(selectionMode==null || id==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson("Brak wymaganych parametrów do pobrania zasobów!"));
        ResponseEntity<?> response;
        switch (selectionMode){
            case "project":
                response = tasksService.getForProjectPageable(id,pageable);
                break;
            case "team":
                response = tasksService.getForTeamPageable(id,pageable);
                break;
            default:
                response = ResponseEntity.ok().build();
                break;
        }
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return tasksService.delete(id);
    }
}
