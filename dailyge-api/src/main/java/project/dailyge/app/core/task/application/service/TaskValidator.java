package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.CommonException;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.*;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TOO_MANY_TASKS;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final MonthlyTaskEntityReadRepository monthlyTaskReadRepository;
    private final TaskEntityReadRepository taskReadRepository;

    public void validateAuth(
        final DailygeUser dailygeUser,
        final TaskJpaEntity findTask
    ) {
        if (dailygeUser.isAdmin()) {
            return;
        }
        if (!findTask.isOwner(dailygeUser.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
    }

    public void validateMonthlyPlan(
        final Long userId,
        final LocalDate date
    ) {
        final boolean yearPlanExists = monthlyTaskReadRepository.existsMonthlyPlanByUserIdAndDate(userId, date);
        if (yearPlanExists) {
            throw TaskTypeException.from(MONTHLY_TASK_EXISTS);
        }
    }

    public void validateTaskCreation(
        final Long userId,
        final LocalDate date
    ) {
        final long todayTaskCount = taskReadRepository.countTodayTask(userId, date);
        if (todayTaskCount >= 10) {
            throw TaskTypeException.from(TOO_MANY_TASKS);
        }
    }
}
