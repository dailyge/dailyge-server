package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;

public record TaskUpdateCommand(
    String monthlyTaskId,
    String title,
    String content,
    LocalDate date,
    TaskStatus status
) {

    public String getStatus() {
        return status.name();
    }
}
