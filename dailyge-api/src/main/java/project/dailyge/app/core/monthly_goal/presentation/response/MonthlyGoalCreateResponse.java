package project.dailyge.app.core.monthly_goal.presentation.response;

import lombok.Getter;

@Getter
public class MonthlyGoalCreateResponse {

    private Long monthlyGoalId;

    private MonthlyGoalCreateResponse() {
    }

    public MonthlyGoalCreateResponse(final Long monthlyGoalId) {
        this.monthlyGoalId = monthlyGoalId;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"monthlyGoalId\":\"%s\"}",
            monthlyGoalId != null ? monthlyGoalId : ""
        );
    }
}
