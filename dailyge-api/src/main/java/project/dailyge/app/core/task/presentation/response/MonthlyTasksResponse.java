package project.dailyge.app.core.task.presentation.response;


import java.time.LocalDate;
import java.util.List;

public class MonthlyTasksResponse {

    private Long monthlyTaskId;
    private int year;
    private int month;
    private List<TaskDetailResponse> tasks;

    private MonthlyTasksResponse() {
    }

    public MonthlyTasksResponse(
        final LocalDate date,
        final List<TaskDetailResponse> tasks
    ) {
        this.monthlyTaskId = getMonthlyTaskId(tasks);
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.tasks = tasks;
    }

    private Long getMonthlyTaskId(final List<TaskDetailResponse> tasks) {
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(0)
            .getMonthlyTaskId();
    }

    public static MonthlyTasksResponse from(
        final LocalDate date,
        final List<TaskDetailResponse> tasks
    ) {
        return new MonthlyTasksResponse(date, tasks);
    }

    public Long getMonthlyTaskId() {
        return monthlyTaskId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public List<TaskDetailResponse> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"monthlyTaskId\":%d,\"year\":%d,\"month\":%d,\"tasks\":%s}",
            monthlyTaskId, year, month, tasks
        );
    }
}
