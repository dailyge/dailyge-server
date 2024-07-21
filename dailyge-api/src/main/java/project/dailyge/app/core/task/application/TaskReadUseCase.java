package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;

public interface TaskReadUseCase {
    TaskJpaEntity findById(DailygeUser dailygeUser, Long taskId);

    MonthlyTaskDocument findMonthlyTaskById(DailygeUser dailygeUser, String monthlyTaskId);

    MonthlyTaskDocument findMonthlyTaskByUserIdAndDate(DailygeUser dailygeUser, LocalDate date);
}
