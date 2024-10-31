package project.dailyge.app.core.monthlygoal.presentation.response;

public class MonthlyGoalCreateResponse {

    private Long monthlyGoalId;

    private MonthlyGoalCreateResponse() {
    }

    public MonthlyGoalCreateResponse(final Long monthlyGoalId) {
        this.monthlyGoalId = monthlyGoalId;
    }

    public Long getMonthlyGoalId() {
        return monthlyGoalId;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"monthlyGoalId\":\"%s\"}",
            monthlyGoalId != null ? monthlyGoalId : ""
        );
    }
}
