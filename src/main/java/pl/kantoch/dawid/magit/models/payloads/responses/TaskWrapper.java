package pl.kantoch.dawid.magit.models.payloads.responses;

import pl.kantoch.dawid.magit.models.Task;

public class TaskWrapper
{
    private Task task;
    private String todayUserTasks;
    private String todayTeamTasks;

    public TaskWrapper() {
    }

    public TaskWrapper(Task task, String todayUserTasks, String todayTeamTasks) {
        this.task = task;
        this.todayUserTasks = todayUserTasks;
        this.todayTeamTasks = todayTeamTasks;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTodayUserTasks() {
        return todayUserTasks;
    }

    public void setTodayUserTasks(String todayUserTasks) {
        this.todayUserTasks = todayUserTasks;
    }

    public String getTodayTeamTasks() {
        return todayTeamTasks;
    }

    public void setTodayTeamTasks(String todayTeamTasks) {
        this.todayTeamTasks = todayTeamTasks;
    }
}
