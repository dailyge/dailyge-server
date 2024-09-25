package project.dailyge.app.core.task.presentation.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import project.dailyge.dto.task.TaskStatisticDto;
import project.dailyge.entity.task.TaskStatus;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Getter
public class MonthlyTasksStatisticResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Map<LocalDate, TaskCountByDateResponse> monthlyStatistic;

    private MonthlyTasksStatisticResponse() {
    }

    public MonthlyTasksStatisticResponse(
        final LocalDate startDate,
        final LocalDate endDate,
        final List<TaskStatisticDto> tasks
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        monthlyStatistic = countTasksByDate(tasks);
    }

    public Map<LocalDate, TaskCountByDateResponse> countTasksByDate(final List<TaskStatisticDto> tasks) {
        return tasks.stream().collect(
            groupingBy(TaskStatisticDto::date,
                collectingAndThen(
                    toList(),
                    oneDayTasks -> {
                        final int successCount = (int) oneDayTasks.stream()
                            .filter(task -> task.status() == TaskStatus.DONE)
                            .count();
                        final int failedCount = oneDayTasks.size() - successCount;
                        return new TaskCountByDateResponse(successCount, failedCount);
                    }
                )
            )
        );
    }
}
