package project.dailyge.app.core.task.application.command;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.document.task.TaskDocument;
import static project.dailyge.entity.task.TaskStatus.TODO;

import java.time.LocalDate;

public record TaskCreateCommand(
    String monthlyTaskId,
    String title,
    String content,
    LocalDate date
) {

    public TaskDocument toDocument(final DailygeUser user) {
        return new TaskDocument(
            monthlyTaskId,
            title,
            content,
            date,
            TODO.name(),
            user.getUserId()
        );
    }
}
