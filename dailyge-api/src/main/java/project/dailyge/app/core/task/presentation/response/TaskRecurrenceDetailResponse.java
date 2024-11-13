package project.dailyge.app.core.task.presentation.response;

import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.util.List;

public class TaskRecurrenceDetailResponse {
    private Long taskRecurrenceId;
    private RecurrenceType type;
    private String title;
    private List<Integer> datePattern;
    private String startDate;
    private String endDate;

    private TaskRecurrenceDetailResponse(final TaskRecurrenceJpaEntity taskRecurrence) {
        this.taskRecurrenceId = taskRecurrence.getId();
        this.type = taskRecurrence.getRecurrenceType();
        this.title = taskRecurrence.getTitle();
        this.datePattern = taskRecurrence.getDatePattern();
        this.startDate = taskRecurrence.getStartDateAsString();
        this.endDate = taskRecurrence.getEndDateAsString();
    }

    public static TaskRecurrenceDetailResponse from(final TaskRecurrenceJpaEntity taskRecurrence) {
        return new TaskRecurrenceDetailResponse(taskRecurrence);
    }

    public Long getTaskRecurrenceId() {
        return taskRecurrenceId;
    }

    public RecurrenceType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getDatePattern() {
        return datePattern;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"type\":\"%s\",\"title\":\"%s\",\"datePattern\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
            taskRecurrenceId, type, title, datePattern, startDate, endDate
        );
    }
}
