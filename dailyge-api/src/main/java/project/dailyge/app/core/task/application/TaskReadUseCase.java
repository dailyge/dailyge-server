package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocument;

import java.time.LocalDate;

public interface TaskReadUseCase {
    MonthlyTaskDocument findMonthlyTaskByUserIdAndDate(DailygeUser dailygeUser, LocalDate date);

    MonthlyTaskDocument findMonthlyTaskById(DailygeUser dailygeUser, String monthlyTaskId);

    TaskDocument findByIdAndDate(DailygeUser dailygeUser, String taskId, LocalDate date);
}
