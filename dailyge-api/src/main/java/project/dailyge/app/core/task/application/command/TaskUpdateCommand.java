package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;

public record TaskUpdateCommand(
    String title,
    String content,
    LocalDate date,
    TaskStatus status,
    TaskColor color
) {
}
