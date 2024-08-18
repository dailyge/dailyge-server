package project.dailyge.app.core.monthlygoal.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;

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

    private MonthlyGoalCreateRequest() {
    }

    public MonthlyGoalCreateRequest(
        final String title,
        final String content
    ) {
        this.title = title;
        this.content = content;
    }

    public MonthlyGoalCreateCommand toCommand() {
        return new MonthlyGoalCreateCommand(title, content);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\"}",
            title != null ? title : "",
            content != null ? content : ""
        );
    }
}
