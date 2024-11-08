package project.dailyge.app.core.task.presentation.response;

public class TaskLabelCreateResponse {

    private Long taskLabelId;

    private TaskLabelCreateResponse() {
    }

    public TaskLabelCreateResponse(final Long taskLabelId) {
        this.taskLabelId = taskLabelId;
    }

    public Long getTaskLabelId() {
        return taskLabelId;
    }

    @Override
    public String toString() {
        return String.format("{\"labelId\":\"%s\"}", taskLabelId);
    }
}
