package project.dailyge.entity.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import project.dailyge.dto.task.TaskStatisticDto;

public interface TaskEntityReadRepository {
    Optional<TaskJpaEntity> findTaskById(Long taskId);

    Optional<MonthlyTaskJpaEntity> findMonthlyTaskByUserIdAndDate(Long userId, LocalDate now);

    long countTodayTask(Long userId, LocalDate date);

    boolean existsMonthlyPlanByUserIdAndDate(Long userId, LocalDate date);

    List<TaskJpaEntity> findTasksByMonthlyTaskIdAndDates(Long userId, Set<Long> monthlyTaskIds, LocalDate startDate, LocalDate endDate);

    List<TaskJpaEntity> findMonthlyTasksByIdAndDate(Long monthlyTaskId, LocalDate date);

    List<TaskStatisticDto> findTaskStatisticByMonthlyTaskIdAndDates(Long userId, Set<Long> monthlyTaskIds, LocalDate startDate, LocalDate endDate);
}
