package project.dailyge.entity.task;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface MonthlyTaskEntityReadRepository {
    Optional<MonthlyTaskJpaEntity> findMonthlyTaskById(Long monthlyTaskId);

    Optional<MonthlyTaskJpaEntity> findMonthlyTaskByUserIdAndDate(Long userId, LocalDate date);

    boolean existsMonthlyPlanByUserIdAndDate(Long userId, LocalDate date);

    Long findMonthlyTaskIdByUserIdAndDate(Long userId, LocalDate date);

    Set<Long> findMonthlyTasksByUserIdAndDates(Long userId, LocalDate startDate, LocalDate endDate);

    long countMonthlyTask(Long userId, LocalDate date);

    Map<YearMonth, Long> findMonthlyTasksMapByUserIdAndDates(Long userId, LocalDate startDate, LocalDate endDate);
}
