package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.TaskColor;

public record TaskRecurrenceUpdateCommand(
    String title,
    String content,
    TaskColor color
) {

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"color\":\"%s\"}",
            title, content, color
        );
    }
}
