package project.dailyge.app.core.task.presentation.requesst;

import jakarta.validation.constraints.NotNull;
import project.dailyge.app.core.task.application.command.TaskStatusUpdateCommand;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;

public record TaskStatusUpdateRequest(
    @NotNull(message = "날짜를 입력해 주세요.")
    LocalDate date,

    @NotNull(message = "Task 상태를 입력해 주세요.")
    TaskStatus status
) {

    public TaskStatusUpdateCommand toCommand() {
        return new TaskStatusUpdateCommand(date, status);
    }

    @Override
    public String toString() {
        return String.format("{\"date\": \"%s\", \"status\":\"%s\"}", date, status);
    }
}
