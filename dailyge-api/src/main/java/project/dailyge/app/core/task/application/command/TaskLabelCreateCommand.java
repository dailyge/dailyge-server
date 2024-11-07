package project.dailyge.app.core.task.application.command;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.task.TaskLabelJpaEntity;

public record TaskLabelCreateCommand(
    String name,
    String description,
    String color
) {

    public TaskLabelJpaEntity toEntity(
        final DailygeUser user
    ) {
        return new TaskLabelJpaEntity(
            name,
            description,
            color,
            user.getUserId()
        );
    }

    @Override
    public String toString() {
        return String.format(
            "{\"name\":\"%s\",\"description\":\"%s\",\"color\":\"%s\"}",
            name, description, color
        );
    }
}
