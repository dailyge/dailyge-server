package project.dailyge.app.core.monthly_goal.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalUpdateCommand;

@Getter
public class MonthlyGoalUpdateRequest {

    @NotNull
    @NotBlank
    @Size(max = 30)
    private String title;

    @NotNull
    @NotBlank
    @Size(max = 1500)
    private String content;

    private MonthlyGoalUpdateRequest() {
    }

    public MonthlyGoalUpdateRequest(
        final String title,
        final String content
    ) {
        this.title = title;
        this.content = content;
    }

    public MonthlyGoalUpdateCommand toCommand() {
        return new MonthlyGoalUpdateCommand(title, content);
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
