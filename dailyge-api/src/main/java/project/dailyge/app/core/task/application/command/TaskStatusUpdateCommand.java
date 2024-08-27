package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;

public record TaskStatusUpdateCommand(
    LocalDate date,
    TaskStatus status
) {

    public String getStatus() {
        return status.name();
    }
}
