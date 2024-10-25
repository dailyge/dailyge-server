package project.dailyge.app.core.weeklygoal.presentation.response;

public record WeeklyGoalCreateResponse(Long weeklyGoalId) {

    @Override
    public String toString() {
        return String.format(
            "{\"weeklyGoalId\":\"%s\"}",
            weeklyGoalId
        );
    }
}
