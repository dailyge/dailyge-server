package project.dailyge.document.task;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskDocumentReadRepository {
    Optional<MonthlyTaskDocument> findMonthlyTaskById(String monthlyTaskId);

    Optional<TaskDocument> findTaskDocumentByIdsAndDate(Long userId, String taskId, LocalDate date);

    Optional<TaskDocument> findTaskDocumentByIds(String monthlyTaskId, String taskId);

    Optional<TaskActivity> findTaskDocumentByIds(Long userId, String monthlyTaskId, String taskId);

    Optional<MonthlyTaskDocument> findMonthlyDocumentByUserIdAndDate(Long userId, LocalDate date);

    long countMonthlyTask(Long userId, LocalDate date);

    long countTodayTask(Long userId, LocalDate date);
}
