package project.dailyge.app.core.task.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;

public interface TaskRecurrenceWriteService {
    Long save(DailygeUser dailygeUser, TaskRecurrenceCreateCommand command);
}
