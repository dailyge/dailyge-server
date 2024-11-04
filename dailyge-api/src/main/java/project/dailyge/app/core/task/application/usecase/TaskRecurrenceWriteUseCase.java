package project.dailyge.app.core.task.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.RecurrenceTasks;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceEntityWriteRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskRecurrenceWriteUseCase implements TaskRecurrenceWriteService {

    private final TaskRecurrenceEntityWriteRepository taskRecurrenceEntityWriteRepository;
    private final TaskEntityWriteRepository taskEntityWriteRepository;
    private final MonthlyTaskEntityReadRepository monthlyTaskEntityReadRepository;

    public TaskRecurrenceWriteUseCase(
        final TaskRecurrenceEntityWriteRepository taskRecurrenceEntityWriteRepository,
        final TaskEntityWriteRepository taskEntityWriteRepository,
        final MonthlyTaskEntityReadRepository monthlyTaskEntityReadRepository
    ) {
        this.taskRecurrenceEntityWriteRepository = taskRecurrenceEntityWriteRepository;
        this.taskEntityWriteRepository = taskEntityWriteRepository;
        this.monthlyTaskEntityReadRepository = monthlyTaskEntityReadRepository;
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
}
