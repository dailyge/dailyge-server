package project.dailyge.app.core.task.presentation.response;

public class TaskRecurrenceIdResponse {

    private Long taskRecurrenceId;

    private TaskRecurrenceIdResponse() {
    }

    public TaskRecurrenceIdResponse(final Long taskRecurrenceId) {
        this.taskRecurrenceId = taskRecurrenceId;
    }

    public Long getTaskRecurrenceId() {
        return taskRecurrenceId;
    }

    @Override
    public String toString() {
        return String.format("{\"taskRecurrenceId\":\"%s\"}", taskRecurrenceId);
    }
}
