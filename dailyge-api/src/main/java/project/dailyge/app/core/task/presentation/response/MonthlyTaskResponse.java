package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;
import project.dailyge.document.task.MonthlyTaskDocument;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MonthlyTaskResponse {

    private String id;
    private Long userId;
    private int year;
    private int month;
    private List<TaskDocumentResponse> tasks;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private MonthlyTaskResponse() {
    }

    public MonthlyTaskResponse(final MonthlyTaskDocument monthlyTaskDocument) {
        this.id = monthlyTaskDocument.getId();
        this.userId = monthlyTaskDocument.getUserId();
        this.year = monthlyTaskDocument.getYear();
        this.month = monthlyTaskDocument.getMonth();
        this.tasks = convert(monthlyTaskDocument);
        this.createdAt = monthlyTaskDocument.getCreatedAt();
        this.lastModifiedAt = monthlyTaskDocument.getLastModifiedAt();
    }

    public static MonthlyTaskResponse from(final MonthlyTaskDocument monthlyTaskDocument) {
        return new MonthlyTaskResponse(monthlyTaskDocument);
    }

    private static List<TaskDocumentResponse> convert(final MonthlyTaskDocument monthlyTaskDocument) {
        if (!monthlyTaskDocument.hasTasks()) {
            return Collections.emptyList();
        }
        return monthlyTaskDocument.getTasks().stream()
            .map(TaskDocumentResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"userId\":%d,\"year\":%d,\"month\":%d,\"tasks\":%s,\"createdAt\":\"%s\",\"lastModifiedAt\":\"%s\"}",
            id, userId, year, month, tasks, createdAt, lastModifiedAt
        );
    }
}
