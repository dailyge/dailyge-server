package project.dailyge.app.core.task.presentation.requesst;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import project.dailyge.app.core.task.application.command.TaskLabelCreateCommand;

public record TaskLabelCreateRequest(
    @Length(min = 1, max = 150)
    @NotNull(message = "제목을 입력해 주세요.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    String name,

    @Length(min = 1, max = 2500)
    @NotNull(message = "내용을 입력해 주세요.")
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    String description,

    @Length(min = 6, max = 6)
    String color
) {

    public TaskLabelCreateCommand toCommand() {
        return new TaskLabelCreateCommand(
            name,
            description,
            color
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
