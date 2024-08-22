package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class WeeklyTasksResponse {
    private Long monthlyTaskId;
    private List<TaskDetailResponse> tasks;

    private WeeklyTasksResponse() {
    }

    public WeeklyTasksResponse(final List<TaskDetailResponse> tasks) {
        this.monthlyTaskId = getMonthlyTaskId(tasks);
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

    @Override
    public String toString() {
        return String.format(
            "{\"monthlyTaskId\":%d,\"tasks\":%s}",
            monthlyTaskId, tasks
        );
    }
}

