package pl.kantoch.dawid.magit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.Task;
import pl.kantoch.dawid.magit.services.TasksService;
import pl.kantoch.dawid.magit.utils.GsonInstance;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController
{
    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Task task){
        return tasksService.save(task);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Task task){
        return tasksService.save(task);
    }

    @PostMapping("/save-subtasks")
    public ResponseEntity<?> saveSubtasks(@RequestBody List<Task> tasks){
        return tasksService.saveSubtasks(tasks);
    }

    @PostMapping("/edit-subtasks/{parentTaskId}")
    public ResponseEntity<?> editSubtasks(@RequestBody List<Task> tasks,@PathVariable Long parentTaskId){
        return tasksService.editSubtasks(tasks,parentTaskId);
    }

    @GetMapping("/{selectionMode}/{id}")
    public ResponseEntity<?> getTasksNoPage(@PathVariable String selectionMode,@PathVariable Long id){
        if(selectionMode==null || id==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak wymaganych parametrów do pobrania zasobów!"));
        ResponseEntity<?> response;
        switch (selectionMode){
            case "project":
                response = tasksService.getForProjectNoPage(id);
                break;
            case "team":
                response = tasksService.getForTeamNoPage(id);
                break;
            case "organisation":
                response = tasksService.getForOrganisationNoPage(id);
                break;
            default:
                response = ResponseEntity.ok().build();
                break;
        }
        return response;
    }

    @GetMapping("/subtasks/{id}")
    public ResponseEntity<?> getSubtasks(@PathVariable Long id){
        if(id==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak wymaganych parametrów do pobrania zasobów!"));
        return tasksService.getSubtasks(id);
    }

    @GetMapping("/set-status/{id}/{status}")
    public ResponseEntity<?> setTaskStatus(@PathVariable Long id,@PathVariable String status){
        if(id==null || status==null || status.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak wymaganych parametrów do wykonania zmiany statusu!"));
        if(status.equals("REALIZACJA")) return tasksService.setTaskStatusAsInRealization(id);
        else return tasksService.setTaskStatusAsComplete(id);
    }

    @GetMapping("/pageable/{selectionMode}/{id}")
    public ResponseEntity<?> getTasksPageable(@PathVariable String selectionMode, @PathVariable Long id, Pageable pageable,@RequestParam(required = false) String query){
        if(selectionMode==null || id==null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GsonInstance.get().toJson("Brak wymaganych parametrów do pobrania zasobów!"));
        ResponseEntity<?> response;
        switch (selectionMode){
            case "project":
                response = tasksService.getForProjectPageable(id,pageable,query);
                break;
            case "team":
                response = tasksService.getForTeamPageable(id,pageable,query);
                break;
            case "organisation":
                response = tasksService.getForOrganisationPageable(id,pageable,query);
                break;
            default:
                response = ResponseEntity.ok().build();
                break;
        }
        return response;
    }

    @GetMapping("/wrapper/{id}")
    public ResponseEntity<?> getTaskWrapper(@PathVariable Long id){
        return tasksService.getTaskWrapper(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return tasksService.delete(id);
    }
}
