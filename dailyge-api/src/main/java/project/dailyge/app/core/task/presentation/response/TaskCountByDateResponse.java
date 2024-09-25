package project.dailyge.app.core.task.presentation.response;

public record TaskCountByDateResponse(
    int successCount,
    int failedCount
) {
}
