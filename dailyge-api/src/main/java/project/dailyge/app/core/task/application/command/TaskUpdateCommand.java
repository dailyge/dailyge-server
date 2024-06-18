package project.dailyge.app.core.task.application.command;

import java.time.LocalDate;

public record TaskUpdateCommand(
    String title,
    String content,
    LocalDate date
) {
}
