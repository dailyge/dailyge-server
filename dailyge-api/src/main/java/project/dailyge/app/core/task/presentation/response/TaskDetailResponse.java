package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;

@Getter
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
