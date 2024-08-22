package project.dailyge.entity.task;

import java.time.LocalDate;
import java.util.Optional;

public interface MonthlyTaskEntityReadRepository {
    Optional<MonthlyTaskJpaEntity> findMonthlyTaskById(Long monthlyTaskId);

    Optional<MonthlyTaskJpaEntity> findMonthlyTaskByUserIdAndDate(Long userId, LocalDate date);

    boolean existsMonthlyPlanByUserIdAndDate(Long userId, LocalDate date);

    Long findMonthlyTaskIdByUserIdAndDate(Long userId, LocalDate date);
}
