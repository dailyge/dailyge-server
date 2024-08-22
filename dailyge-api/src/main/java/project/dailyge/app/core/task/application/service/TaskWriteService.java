package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskStatusUpdateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.MonthlyTaskEntityWriteRepository;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import static project.dailyge.entity.task.MonthlyTasks.createMonthlyTasks;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.List;

@ApplicationLayer
@RequiredArgsConstructor
class TaskWriteService implements TaskWriteUseCase {

    private final TaskValidator validator;
    private final TaskEntityReadRepository taskReadRepository;
    private final TaskEntityWriteRepository taskWriteRepository;
    private final MonthlyTaskEntityReadRepository monthlyTaskReadRepository;
    private final MonthlyTaskEntityWriteRepository monthlyTaskWriteRepository;

    @Override
    @Transactional
    public void saveAll(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        validator.validateMonthlyPlan(dailygeUser.getUserId(), date);
        final List<MonthlyTaskJpaEntity> monthlyTasks = createMonthlyTasks(dailygeUser.getId(), date.getYear());
        monthlyTaskWriteRepository.saveAll(monthlyTasks);
    }

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final TaskCreateCommand command
    ) {
        validator.validateTaskCreation(dailygeUser.getId(), command.date());
        final MonthlyTaskJpaEntity findMonthlyTask = monthlyTaskReadRepository.findMonthlyTaskById(command.monthlyTaskId())
            .orElseThrow(() -> TaskTypeException.from(MONTHLY_TASK_NOT_FOUND));
        final TaskJpaEntity newTask = command.toEntity(dailygeUser, findMonthlyTask.getId());
        return taskWriteRepository.save(newTask);
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long taskId,
        final TaskUpdateCommand command
    ) {
        final TaskJpaEntity findTask = taskReadRepository.findTaskById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        dailygeUser.validateAuth(findTask.getUserId());
        findTask.update(command.title(), command.content(), command.date(), command.status());
    }

    @Override
    @Transactional
    public void updateStatus(
        final DailygeUser dailygeUser,
        final Long taskId,
        final TaskStatusUpdateCommand command
    ) {
        final TaskJpaEntity findTask = taskReadRepository.findTaskById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        dailygeUser.validateAuth(findTask.getUserId());
        findTask.updateStatus(command.status());
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadRepository.findTaskById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        dailygeUser.validateAuth(findTask.getUserId());
        findTask.delete();
    }
}
