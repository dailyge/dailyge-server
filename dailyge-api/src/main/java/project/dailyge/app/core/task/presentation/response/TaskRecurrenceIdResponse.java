package project.dailyge.app.core.task.presentation.response;

public record TaskRecurrenceIdResponse(Long taskRecurrenceId) {

    @Override
    public String toString() {
        return String.format("{\"taskRecurrenceId\":\"%s\"}", taskRecurrenceId);
    }
}
