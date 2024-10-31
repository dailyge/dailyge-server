package project.dailyge.app.core.task.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskLabelCreateCommand;
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
import project.dailyge.entity.task.TaskLabelEntityWriteRepository;
import project.dailyge.entity.task.TaskLabelJpaEntity;

@ApplicationLayer(value = "TaskWriteUseCase")
class TaskWriteUseCase implements TaskWriteService {

    private final TaskValidator validator;
    private final TaskEntityReadRepository taskReadRepository;
    private final TaskEntityWriteRepository taskWriteRepository;
    private final MonthlyTaskEntityReadRepository monthlyTaskReadRepository;
    private final MonthlyTaskEntityWriteRepository monthlyTaskWriteRepository;
    private final TaskLabelEntityWriteRepository taskLabelEntityWriteRepository;

    public TaskWriteUseCase(
        final TaskValidator validator,
        final TaskEntityReadRepository taskReadRepository,
        final TaskEntityWriteRepository taskWriteRepository,
        final MonthlyTaskEntityReadRepository monthlyTaskReadRepository,
        final MonthlyTaskEntityWriteRepository monthlyTaskWriteRepository,
        final TaskLabelEntityWriteRepository taskLabelEntityWriteRepository
    ) {
        this.validator = validator;
        this.taskReadRepository = taskReadRepository;
        this.taskWriteRepository = taskWriteRepository;
        this.monthlyTaskReadRepository = monthlyTaskReadRepository;
        this.monthlyTaskWriteRepository = monthlyTaskWriteRepository;
        this.taskLabelEntityWriteRepository = taskLabelEntityWriteRepository;
    }

    public TaskWriteUseCase(
        final TaskValidator validator,
        final TaskEntityReadRepository taskReadRepository,
        final TaskEntityWriteRepository taskWriteRepository,
        final MonthlyTaskEntityReadRepository monthlyTaskReadRepository,
        final MonthlyTaskEntityWriteRepository monthlyTaskWriteRepository
    ) {
        this.validator = validator;
        this.taskReadRepository = taskReadRepository;
        this.taskWriteRepository = taskWriteRepository;
        this.monthlyTaskReadRepository = monthlyTaskReadRepository;
        this.monthlyTaskWriteRepository = monthlyTaskWriteRepository;
    }

    public TaskWriteUseCase(
        final TaskValidator validator,
        final TaskEntityReadRepository taskReadRepository,
        final TaskEntityWriteRepository taskWriteRepository,
        final MonthlyTaskEntityReadRepository monthlyTaskReadRepository,
        final MonthlyTaskEntityWriteRepository monthlyTaskWriteRepository
    ) {
        this.validator = validator;
        this.taskReadRepository = taskReadRepository;
        this.taskWriteRepository = taskWriteRepository;
        this.monthlyTaskReadRepository = monthlyTaskReadRepository;
        this.monthlyTaskWriteRepository = monthlyTaskWriteRepository;
    }

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
        final MonthlyTaskJpaEntity findMonthlyTask = findMonthlyTaskById(dailygeUser, command.date());
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
        final MonthlyTaskJpaEntity findMonthlyTask = findMonthlyTaskById(dailygeUser, command.date());
        final TaskJpaEntity findTask = taskReadRepository.findTaskById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        dailygeUser.validateAuth(findTask.getUserId());
        findTask.update(command.title(), command.content(), command.date(), command.status(), findMonthlyTask.getId(), command.color());
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

    @Override
    @Transactional
    public Long saveTaskLabel(
        final DailygeUser dailygeUser,
        final TaskLabelCreateCommand command
    ) {
        validator.validateTaskLabelCreation(dailygeUser.getUserId());
        final TaskLabelJpaEntity taskLabel = command.toEntity(dailygeUser);
        return taskLabelEntityWriteRepository.save(taskLabel);
    }

    private MonthlyTaskJpaEntity findMonthlyTaskById(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        return monthlyTaskReadRepository.findMonthlyTaskByUserIdAndDate(dailygeUser.getUserId(), date)
            .orElseThrow(() -> TaskTypeException.from(MONTHLY_TASK_NOT_FOUND));
    }
}
