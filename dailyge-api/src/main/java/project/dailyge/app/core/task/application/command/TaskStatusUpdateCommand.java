package project.dailyge.app.core.task.application.command;

import project.dailyge.entity.task.TaskStatus;

public record TaskStatusUpdateCommand(
    String monthlyTaskId,
    TaskStatus status
) {

    public String getStatus() {
        return status.name();
    }
}
