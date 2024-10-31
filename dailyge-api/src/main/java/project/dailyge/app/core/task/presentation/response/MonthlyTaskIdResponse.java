package project.dailyge.app.core.task.presentation.response;

public class MonthlyTaskIdResponse {

    private Long monthlyTaskId;

    private MonthlyTaskIdResponse() {
    }

    public MonthlyTaskIdResponse(final Long monthlyTaskId) {
        this.monthlyTaskId = monthlyTaskId;
    }

    public Long getMonthlyTaskId() {
        return monthlyTaskId;
    }

    @Override
    public String toString() {
        return String.format("{\"monthlyTaskId\":\"%s\"}", monthlyTaskId);
    }
}
