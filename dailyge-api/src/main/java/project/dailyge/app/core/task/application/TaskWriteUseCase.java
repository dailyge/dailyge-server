package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import project.dailyge.document.task.TaskDocument;

import java.time.LocalDate;

public interface TaskWriteUseCase {
    void createMonthlyTasks(DailygeUser dailygeUser, LocalDate date);

    String save(TaskDocument task);

    void update(DailygeUser dailygeUser, Long taskId, TaskUpdateCommand command);

    void delete(DailygeUser dailygeUser, Long taskId);
}
