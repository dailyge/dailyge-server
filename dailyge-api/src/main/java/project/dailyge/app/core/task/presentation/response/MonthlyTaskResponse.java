package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MonthlyTaskResponse {

    private Long monthlyTaskId;
    private int year;
    private int month;
    private List<TaskDetailResponse> tasks;

    private MonthlyTaskResponse() {
    }

    public MonthlyTaskResponse(
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

    public static MonthlyTaskResponse from(
        final LocalDate date,
        final List<TaskDetailResponse> tasks
    ) {
        return new MonthlyTaskResponse(date, tasks);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"monthlyTaskId\":%d,\"year\":%d,\"month\":%d,\"tasks\":%s}",
            monthlyTaskId, year, month, tasks
        );
    }
}
