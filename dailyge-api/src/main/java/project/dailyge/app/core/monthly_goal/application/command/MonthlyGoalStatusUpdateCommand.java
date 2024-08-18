package project.dailyge.app.core.monthly_goal.application.command;

public record MonthlyGoalStatusUpdateCommand(
    Boolean done
) {

    @Override
    public String toString() {
        return String.format(
            "{\"done\":\"%s\"}",
            done != null ? done : ""
        );
    }
}
