package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.List;

public interface TaskReadUseCase {
    Long findMonthlyTaskId(DailygeUser dailygeUser, LocalDate date);

    TaskJpaEntity findTaskById(DailygeUser dailygeUser, Long taskId);

    MonthlyTaskJpaEntity findMonthlyTaskById(Long monthlyTaskId);

    List<TaskJpaEntity> findTasksByMonthlyTaskIdAndDates(DailygeUser dailygeUser, LocalDate startDate, LocalDate endDate);

    MonthlyTaskJpaEntity findMonthlyTaskByUserIdAndDate(DailygeUser dailygeUser, LocalDate date);

    List<TaskJpaEntity> findTasksByMonthlyTasksIdAndDate(DailygeUser dailygeUser, LocalDate date);
}
