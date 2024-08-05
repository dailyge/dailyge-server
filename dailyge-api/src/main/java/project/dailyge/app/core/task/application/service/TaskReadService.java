package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.Application;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;

import java.time.LocalDate;

@Application
@RequiredArgsConstructor
class TaskReadService implements TaskReadUseCase {

    private final TaskValidator validator;
    private final TaskDocumentReadRepository taskReadRepository;

    @Override
    public MonthlyTaskDocument findMonthlyTaskByUserIdAndDate(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        final MonthlyTaskDocument findTask = taskReadRepository.findMonthlyDocumentByUserIdAndDate(dailygeUser.getId(), date)
            .orElseThrow(() -> TaskTypeException.from(MONTHLY_TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTask);
        return findTask;
    }

    @Override
    public MonthlyTaskDocument findMonthlyTaskById(
        final DailygeUser dailygeUser,
        final String monthlyTaskId
    ) {
        final MonthlyTaskDocument findTask = taskReadRepository.findMonthlyTaskById(monthlyTaskId)
            .orElseThrow(() -> TaskTypeException.from(MONTHLY_TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTask);
        return findTask;
    }

    @Override
    public TaskDocument findByIdAndDate(
        final DailygeUser dailygeUser,
        final String taskId,
        final LocalDate date
    ) {
        final TaskDocument findTaskDocument = taskReadRepository.findTaskDocumentByIdsAndDate(
            dailygeUser.getId(), taskId, date
        ).orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTaskDocument);
        return findTaskDocument;
    }
}
