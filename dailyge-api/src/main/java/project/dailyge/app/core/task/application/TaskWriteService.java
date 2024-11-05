package project.dailyge.app.core.task.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskLabelCreateCommand;
import project.dailyge.app.core.task.application.command.TaskStatusUpdateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;

import java.time.LocalDate;

public interface TaskWriteService {
    void saveAll(DailygeUser dailygeUser, LocalDate date);

    Long save(DailygeUser dailygeUser, TaskCreateCommand command);

    void update(DailygeUser dailygeUser, Long taskId, TaskUpdateCommand command);

    void updateStatus(DailygeUser dailygeUser, Long taskId, TaskStatusUpdateCommand command);

    void delete(DailygeUser dailygeUser, Long taskId);

    Long saveTaskLabel(DailygeUser dailygeUser, TaskLabelCreateCommand command);
}
