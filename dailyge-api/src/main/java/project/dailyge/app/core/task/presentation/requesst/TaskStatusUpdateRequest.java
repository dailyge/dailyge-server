package project.dailyge.app.core.task.presentation.requesst;

import jakarta.validation.constraints.NotNull;
import project.dailyge.app.core.task.application.command.TaskStatusUpdateCommand;
import project.dailyge.entity.task.TaskStatus;

public record TaskStatusUpdateRequest(
    @NotNull(message = "monthlyId를 입력해 주세요.")
    Long monthlyTaskId,

    @NotNull(message = "Task 상태를 입력해 주세요.")
    TaskStatus status
) {

    public TaskStatusUpdateCommand toCommand() {
        return new TaskStatusUpdateCommand(monthlyTaskId, status);
    }

    @Override
    public String toString() {
        return String.format("{\"monthlyTaskId\":\"%s\",\"status\":\"%s\"}", monthlyTaskId, status);
    }
}
