package project.dailyge.app.core.weeklygoal.request;

public record WeeklyGoalStatusUpdateRequest(
    Boolean done
) {

    @Override
    public String toString() {
        return String.format(
            "{\"done\":\"%s\"}",
            done
        );
    }
}
