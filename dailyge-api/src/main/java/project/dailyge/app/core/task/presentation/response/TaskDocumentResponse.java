package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;
import project.dailyge.document.task.TaskDocument;

import java.time.LocalDateTime;

@Getter
public class TaskDocumentResponse {

    private final String id;
    private final String monthlyTaskId;
    private final String title;
    private final String content;
    private final int day;
    private final int weekOfMonth;
    private final String status;
    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;

    private TaskDocumentResponse(final TaskDocument taskDocument) {
        this.id = taskDocument.getId();
        this.monthlyTaskId = taskDocument.getMonthlyTaskId();
        this.title = taskDocument.getTitle();
        this.content = taskDocument.getContent();
        this.day = taskDocument.getDay();
        this.weekOfMonth = taskDocument.getWeekOfMonth();
        this.status = taskDocument.getStatus();
        this.userId = taskDocument.getUserId();
        this.createdAt = taskDocument.getCreatedAt();
        this.lastModifiedAt = taskDocument.getLastModifiedAt();
    }

    public static TaskDocumentResponse from(final TaskDocument taskDocument) {
        return new TaskDocumentResponse(taskDocument);
    }
}
