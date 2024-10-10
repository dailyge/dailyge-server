package project.dailyge.app.core.weeklygoal.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalUpdateCommand;

public record WeeklyGoalUpdateRequest(

    @NotNull(message = "null 값이 들어갈 수 없습니다.")
    @NotBlank(message = "공백이 들어갈 수 없습니다.")
    @Size(max = 50, message = "제목 길이 제한을 초과했습니다.")
    String title,

    @NotNull(message = "null 값이 들어갈 수 없습니다.")
    @NotBlank(message = "공백이 들어갈 수 없습니다.")
    @Size(max = 1500, message = "내용 길이 제한을 초과했습니다.")
    String content
) {

    public WeeklyGoalUpdateCommand toCommand() {
        return new WeeklyGoalUpdateCommand(title, content);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\"}",
            title, content
        );
    }
}
