package project.dailyge.app.core.task.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.app.core.task.utils.ScheduleUtils;
import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceEntityWriteRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskRecurrenceWriteUseCase implements TaskRecurrenceWriteService {

    private final TaskRecurrenceEntityWriteRepository taskRecurrenceEntityWriteRepository;
    private final TaskEntityWriteRepository taskEntityWriteRepository;

    public TaskRecurrenceWriteUseCase(
        final TaskRecurrenceEntityWriteRepository taskRecurrenceEntityWriteRepository,
        final TaskEntityWriteRepository taskEntityWriteRepository
    ) {
        this.taskRecurrenceEntityWriteRepository = taskRecurrenceEntityWriteRepository;
        this.taskEntityWriteRepository = taskEntityWriteRepository;
    }

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final TaskRecurrenceCreateCommand command
    ) {
        final TaskRecurrenceJpaEntity taskRecurrence = command.toEntity();
        final Long taskRecurrenceId = taskRecurrenceEntityWriteRepository.save(taskRecurrence);
        if (taskRecurrence.getRecurrenceType().equals(RecurrenceType.DAILY)) {
            final List<LocalDateTime> dailySchedules = ScheduleUtils.generateDailySchedules(taskRecurrence.getStartDate(), taskRecurrence.getEndDate());
            saveSchedules(dailygeUser, dailySchedules, taskRecurrence, command);
            return taskRecurrenceId;
        }
        if (taskRecurrence.getRecurrenceType().equals(RecurrenceType.WEEKLY)) {
            final List<LocalDateTime> weeklySchedules = ScheduleUtils.generateWeeklySchedules(
                taskRecurrence.getStartDate(),
                taskRecurrence.getEndDate(),
                taskRecurrence.getDatePattern()
            );
            saveSchedules(dailygeUser, weeklySchedules, taskRecurrence, command);
            return taskRecurrenceId;
        }
        if (taskRecurrence.getRecurrenceType().equals(RecurrenceType.MONTHLY)) {
            final List<LocalDateTime> monthlySchedules = ScheduleUtils.generateMonthlySchedules(
                taskRecurrence.getStartDate(),
                taskRecurrence.getEndDate(),
                taskRecurrence.getDatePattern()
            );
            saveSchedules(dailygeUser, monthlySchedules, taskRecurrence, command);
            return taskRecurrenceId;
        }
        throw new IllegalArgumentException();
    }

    public void saveSchedules(
        final DailygeUser dailygeUser,
        final List<LocalDateTime> schedules,
        final TaskRecurrenceJpaEntity taskRecurrence,
        final TaskRecurrenceCreateCommand command
    ) {
        for (final LocalDateTime schedule : schedules) {
            TaskJpaEntity newTask = new TaskJpaEntity(
                taskRecurrence.getTitle(),
                taskRecurrence.getContent(),
                schedule.toLocalDate(),
                TaskStatus.TODO,
                command.color(),
                null,
                dailygeUser.getUserId(),
                taskRecurrence.getId()
            );
            taskEntityWriteRepository.save(newTask);
        }
    }
}
