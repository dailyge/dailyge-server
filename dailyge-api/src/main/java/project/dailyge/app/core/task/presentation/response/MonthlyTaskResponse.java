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

    private static List<TaskDocumentResponse> convert(final MonthlyTaskDocument monthlyTaskDocument) {
        if (!monthlyTaskDocument.hasTasks()) {
            return Collections.emptyList();
        }
        return monthlyTaskDocument.getTasks().stream()
            .map(TaskDocumentResponse::new)
            .collect(Collectors.toList());
    }
}
