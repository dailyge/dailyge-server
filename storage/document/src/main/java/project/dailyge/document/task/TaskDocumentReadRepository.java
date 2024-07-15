package project.dailyge.document.task;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskDocumentReadRepository {
    Optional<MonthlyTaskDocument> findMonthlyDocumentByUserId(Long userId, LocalDate date);

    boolean existsYearPlanByUserId(Long userId, LocalDate date);

    long countMonthlyTask(Long userId, LocalDate date);

    long countTodayTask(Long userId, LocalDate date);
}
