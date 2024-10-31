package project.dailyge.app.core.monthlygoal.presentation.request;

import jakarta.validation.constraints.NotNull;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalStatusUpdateCommand;

public class MonthlyGoalStatusUpdateRequest {

    @NotNull
    private Boolean done;

    private MonthlyGoalStatusUpdateRequest() {
    }

    public MonthlyGoalStatusUpdateRequest(final Boolean done) {
        this.done = done;
    }

    public Boolean getDone() {
        return done;
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
