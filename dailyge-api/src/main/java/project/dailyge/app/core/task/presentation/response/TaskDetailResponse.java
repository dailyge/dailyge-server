package project.dailyge.app.core.task.presentation.response;

import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;

public class TaskDetailResponse {

    private final Long taskId;
    private final Long monthlyTaskId;
    private final String title;
    private final String content;
    private final int year;
    private final int month;
    private final int day;
    private final int weekOfMonth;
    private final TaskStatus status;
    private final TaskColor color;
    private final Long userId;
    private final String createdAt;
    private final String lastModifiedAt;

    private TaskDetailResponse(final TaskJpaEntity task) {
        this.taskId = task.getId();
        this.monthlyTaskId = task.getMonthlyTaskId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.year = task.getYear();
        this.month = task.getMonth();
        this.day = task.getDay();
        this.weekOfMonth = task.getWeekOfMonth();
        this.status = task.getStatus();
        this.color = task.getColor();
        this.userId = task.getUserId();
        this.createdAt = task.getCreatedAtAsString();
        this.lastModifiedAt = task.getLastModifiedAtAsString();
    }

    public static TaskDetailResponse from(final TaskJpaEntity taskDocument) {
        return new TaskDetailResponse(taskDocument);
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getMonthlyTaskId() {
        return monthlyTaskId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskColor getColor() {
        return color;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"monthlyTaskId\":\"%s\",\"title\":\"%s\",\"content\":\"%s\",\"year\":%d,\"month\":%d,"
                + "\"day\":%d,\"weekOfMonth\":%d,\"status\":\"%s\", \"color\":\"%s\", \"userId\":%d,"
                + "\"createdAt\":\"%s\",\"lastModifiedAt\":\"%s\"}",
            taskId, monthlyTaskId, title, content, year, month, day, weekOfMonth, status, color, userId, createdAt, lastModifiedAt
        );
    }
}
