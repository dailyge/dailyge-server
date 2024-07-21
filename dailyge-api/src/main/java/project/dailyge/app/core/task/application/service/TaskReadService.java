package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
class TaskReadService implements TaskReadUseCase {

    private final TaskValidator validator;
    private final TaskDocumentReadRepository taskReadRepository;

    @Override
    public TaskJpaEntity findById(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
//        final TaskJpaEntity findTask = taskRepository.findById(taskId)
//            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
//        if (!findTask.isOwner(dailygeUser.getUserId())) {
//            throw new UnAuthorizedException();
//        }
        return null;
    }

    @Override
    public MonthlyTaskDocument findMonthlyTaskById(
        final DailygeUser dailygeUser,
        final String monthlyTaskId
    ) {
        final MonthlyTaskDocument findTask = taskReadRepository.findMonthlyTaskById(monthlyTaskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTask);
        return findTask;
    }

    @Override
    public MonthlyTaskDocument findMonthlyTaskByUserIdAndDate(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        final MonthlyTaskDocument findTask = taskReadRepository.findMonthlyDocumentByUserIdAndDate(dailygeUser.getId(), date)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTask);
        return findTask;
    }
}
