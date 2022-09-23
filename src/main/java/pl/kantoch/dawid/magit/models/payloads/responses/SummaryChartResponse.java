package pl.kantoch.dawid.magit.models.payloads.responses;

public class SummaryChartResponse {
    private String projectName;
    private Long taskCount;

    public SummaryChartResponse(String projectName, Long taskCount) {
        this.projectName = projectName;
        this.taskCount = taskCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public Long getTaskCount() {
        return taskCount;
    }
}
