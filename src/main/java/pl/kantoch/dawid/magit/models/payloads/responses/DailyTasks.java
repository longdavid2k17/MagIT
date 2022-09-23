package pl.kantoch.dawid.magit.models.payloads.responses;

import pl.kantoch.dawid.magit.models.Task;

import java.util.List;

public class DailyTasks {
    private String label;
    private List<Task> taskList;

    public DailyTasks() {
    }

    public DailyTasks(String label, List<Task> taskList) {
        this.label = label;
        this.taskList = taskList;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
