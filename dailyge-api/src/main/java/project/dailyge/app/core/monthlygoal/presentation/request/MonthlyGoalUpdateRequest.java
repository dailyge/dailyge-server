package project.dailyge.app.core.monthlygoal.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalUpdateCommand;

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

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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
