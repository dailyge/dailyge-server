package project.dailyge.app.core.task.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.app.core.task.application.command.TaskRecurrenceUpdateCommand;

public interface TaskRecurrenceWriteService {
    Long save(DailygeUser dailygeUser, TaskRecurrenceCreateCommand command);

    void update(DailygeUser dailygeUser, Long taskRecurrenceId, TaskRecurrenceUpdateCommand command);
}
