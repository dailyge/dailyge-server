package project.dailyge.app.core.task.presentation.response;

public class TaskCreateResponse {

    private Long taskId;

    private TaskCreateResponse() {
    }

    public TaskCreateResponse(final Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    @Override
    public String toString() {
        return String.format("{\"taskId\":\"%s\"}", taskId);
    }
}
