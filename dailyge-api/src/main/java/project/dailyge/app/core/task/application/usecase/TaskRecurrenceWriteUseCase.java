package project.dailyge.app.core.task.application.usecase;

import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.app.core.task.application.command.TaskRecurrenceUpdateCommand;
import project.dailyge.app.core.task.exception.TaskCodeAndMessage;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.RecurrenceTasks;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceEntityReadRepository;
import project.dailyge.entity.task.TaskRecurrenceEntityWriteRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationLayer(value = "TaskRecurrenceWriteUseCase")
public class TaskRecurrenceWriteUseCase implements TaskRecurrenceWriteService {

    private final TaskRecurrenceEntityWriteRepository taskRecurrenceEntityWriteRepository;
    private final TaskEntityWriteRepository taskEntityWriteRepository;
    private final MonthlyTaskEntityReadRepository monthlyTaskEntityReadRepository;
    private final TaskRecurrenceEntityReadRepository taskRecurrenceEntityReadRepository;

    public TaskRecurrenceWriteUseCase(
        final TaskRecurrenceEntityWriteRepository taskRecurrenceEntityWriteRepository,
        final TaskEntityWriteRepository taskEntityWriteRepository,
        final MonthlyTaskEntityReadRepository monthlyTaskEntityReadRepository,
        final TaskRecurrenceEntityReadRepository taskRecurrenceEntityReadRepository
    ) {
        this.taskRecurrenceEntityWriteRepository = taskRecurrenceEntityWriteRepository;
        this.taskEntityWriteRepository = taskEntityWriteRepository;
        this.monthlyTaskEntityReadRepository = monthlyTaskEntityReadRepository;
        this.taskRecurrenceEntityReadRepository = taskRecurrenceEntityReadRepository;
    }

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final TaskRecurrenceCreateCommand command
    ) {
        final TaskRecurrenceJpaEntity taskRecurrence = command.toEntity();
        final Long taskRecurrenceId = taskRecurrenceEntityWriteRepository.save(taskRecurrence);
        final Map<YearMonth, Long> monthlyTasksMap = monthlyTaskEntityReadRepository
            .findMonthlyTasksMapByUserIdAndDates(dailygeUser.getUserId(), command.startDate(), command.endDate());
        final RecurrenceTasks recurrenceTasks = new RecurrenceTasks(
            taskRecurrence,
            new ArrayList<>(),
            dailygeUser.getUserId(),
            command.color(),
            monthlyTasksMap
        );
        final List<TaskJpaEntity> tasks = recurrenceTasks.create();
        taskEntityWriteRepository.saveBulks(tasks);
        return taskRecurrenceId;
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long taskRecurrenceId,
        final TaskRecurrenceUpdateCommand command
    ) {
        final TaskRecurrenceJpaEntity findTaskRecurrence = taskRecurrenceEntityReadRepository.findById(taskRecurrenceId)
            .orElseThrow(() -> TaskTypeException.from(TaskCodeAndMessage.TASK_RECURRENCE_NOT_FOUND));
        dailygeUser.validateAuth(findTaskRecurrence.getUserId());
        findTaskRecurrence.update(command.title(), command.content());
        final List<TaskJpaEntity> tasks = taskRecurrenceEntityReadRepository.findTasksAfterStartDateById(taskRecurrenceId, LocalDateTime.now().toLocalDate());
        tasks.stream().forEach(task -> task.update(command.title(), command.content(), task.getDate(), task.getStatus(), task.getMonthlyTaskId(), command.color()));
    }
}
