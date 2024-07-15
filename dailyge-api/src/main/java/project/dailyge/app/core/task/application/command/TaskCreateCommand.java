package project.dailyge.app.core.task.application.command;

import org.bson.types.ObjectId;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;

public record TaskCreateCommand(
    String monthlyTaskId,
    String title,
    String content,
    LocalDate date
) {
    public TaskDocument toDocument(final DailygeUser user) {
        return new TaskDocument(
            new ObjectId().toHexString(),
            this.monthlyTaskId,
            title,
            content,
            date,
            TaskStatus.TODO.name(),
            user.getUserId()
        );
    }
}
