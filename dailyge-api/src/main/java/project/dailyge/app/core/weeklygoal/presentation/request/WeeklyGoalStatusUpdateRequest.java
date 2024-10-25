package project.dailyge.app.core.weeklygoal.presentation.request;

public record WeeklyGoalStatusUpdateRequest(boolean done) {

    @Override
    public String toString() {
        return String.format(
            "{\"done\":\"%s\"}",
            done
        );
    }
}
