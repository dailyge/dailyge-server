package project.dailyge.app.core.monthly_goal.presentation.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalStatusUpdateCommand;

@Getter
public class MonthlyGoalStatusUpdateRequest {

    @NotNull
    private Boolean done;

    private MonthlyGoalStatusUpdateRequest() {
    }

    public MonthlyGoalStatusUpdateRequest(final Boolean done) {
        this.done = done;
    }

    public MonthlyGoalStatusUpdateCommand toCommand() {
        return new MonthlyGoalStatusUpdateCommand(done);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"done\":\"%s\"}",
            done != null ? done : ""
        );
    }
}
