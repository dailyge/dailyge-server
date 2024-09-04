package project.dailyge.app.core.monthlygoal.application.command;

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
