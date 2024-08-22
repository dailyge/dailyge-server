package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;

@Getter
public class MonthlyTaskIdResponse {

    private Long monthlyTaskId;

    private MonthlyTaskIdResponse() {
    }

    public MonthlyTaskIdResponse(final Long monthlyTaskId) {
        this.monthlyTaskId = monthlyTaskId;
    }

    @Override
    public String toString() {
        return String.format("{\"monthlyTaskId\":\"%s\"}", monthlyTaskId);
    }
}
