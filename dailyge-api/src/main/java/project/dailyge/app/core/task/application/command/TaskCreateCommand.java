package project.dailyge.app.core.task.application.command;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskJpaEntity;
import static project.dailyge.entity.task.TaskStatus.TODO;

import java.time.LocalDate;

public record TaskCreateCommand(
    Long monthlyTaskId,
    String title,
    String content,
    TaskColor color,
    LocalDate date
) {

    public TaskJpaEntity toEntity(
        final DailygeUser user,
        final Long monthlyTaskId
    ) {
        return new TaskJpaEntity(
            title,
            content,
            date,
            TODO,
            color,
            monthlyTaskId,
            user.getUserId()
        );
    }
}
