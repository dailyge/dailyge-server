package project.dailyge.app.core.task.application.service;

import static java.time.LocalDate.now;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TOO_MANY_TASKS;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final TaskDocumentReadRepository taskDocumentReadRepository;

    public void validateAuth(final DailygeUser dailygeUser) {
        if (!dailygeUser.isAdmin()) {
            throw new UnAuthorizedException();
        }
    }

    public void validateAuth(
        final DailygeUser dailygeUser,
        final TaskJpaEntity findTask
    ) {
        if (dailygeUser.isAdmin()) {
            return;
        }
        if (!findTask.isOwner(dailygeUser.getUserId())) {
            throw new UnAuthorizedException();
        }
    }

    public void validateYearPlan(final Long userId) {
        final boolean yearPlanExists = taskDocumentReadRepository.existsYearPlanByUserId(userId, now());
        if (yearPlanExists) {
            throw TaskTypeException.from(MONTHLY_TASK_EXISTS);
        }
    }

    public void validateTaskCreation(
        final Long userId,
        final LocalDate date
    ) {
        final long todayTaskCount = taskDocumentReadRepository.countTodayTask(userId, date);
        if (todayTaskCount >= 10) {
            throw TaskTypeException.from(TOO_MANY_TASKS);
        }

        final Optional<MonthlyTaskDocument> findMonthlyTask = taskDocumentReadRepository.findMonthlyDocumentByUserId(userId, now());
        if (findMonthlyTask.isEmpty()) {
            throw TaskTypeException.from(MONTHLY_TASK_NOT_EXISTS);
        }
    }
}
