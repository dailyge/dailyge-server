package project.dailyge.app.core.weeklygoal.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;

import java.time.LocalDate;

public record WeeklyGoalCreateRequest(

    @NotNull(message = "null 값이 들어갈 수 없습니다.")
    @NotBlank(message = "공백이 들어갈 수 없습니다.")
    @Size(max = 50, message = "제목 길이 제한을 초과했습니다.")
    String title,

    @NotNull(message = "null 값이 들어갈 수 없습니다.")
    @NotBlank(message = "공백이 들어갈 수 없습니다.")
    @Size(max = 1500, message = "내용 길이 제한을 초과했습니다.")
    String content,

    @NotNull(message = "날짜를 입력해주세요.")
    LocalDate date
) {

    public WeeklyGoalCreateCommand toCommand() {
        return new WeeklyGoalCreateCommand(title, content, date);
    }
}
