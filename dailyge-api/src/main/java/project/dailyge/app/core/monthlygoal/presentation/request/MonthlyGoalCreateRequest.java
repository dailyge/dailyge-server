package project.dailyge.app.core.monthlygoal.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;

import java.time.LocalDate;

@Getter
public class MonthlyGoalCreateRequest {

    @NotNull
    @NotBlank
    @Size(max = 30)
    private String title;

    @NotNull
    @NotBlank
    @Size(max = 1500)
    private String content;

    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate date;

    private MonthlyGoalCreateRequest() {
    }

    public MonthlyGoalCreateRequest(
        final String title,
        final String content,
        final LocalDate date
    ) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public MonthlyGoalCreateCommand toCommand() {
        return new MonthlyGoalCreateCommand(title, content, date);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\"}",
            title != null ? title : "",
            content != null ? content : "",
            date
        );
    }
}
