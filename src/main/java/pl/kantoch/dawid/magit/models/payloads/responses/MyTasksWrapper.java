package pl.kantoch.dawid.magit.models.payloads.responses;

import java.util.List;

public class MyTasksWrapper
{
    private List<DailyTasks> list;

    public MyTasksWrapper() {
    }

    public MyTasksWrapper(List<DailyTasks> list) {
        this.list = list;
    }

    public List<DailyTasks> getList() {
        return list;
    }

    public void setList(List<DailyTasks> list) {
        this.list = list;
    }
}
