package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.TaskStatus;

public record TaskStatusUpdateCommand(
    Long monthlyTaskId,
    TaskStatus status
) {

    public String getStatus() {
        return status.name();
    }
}
