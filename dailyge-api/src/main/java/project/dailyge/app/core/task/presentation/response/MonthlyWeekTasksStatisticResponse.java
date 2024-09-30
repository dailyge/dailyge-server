package project.dailyge.app.core.task.presentation.response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.Tasks;
import static java.util.Collections.emptyList;

@Getter
public class MonthlyWeekTasksStatisticResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Map<Integer, MonthlyWeeksRateResponse> beforeMonthlyStatistic;
    private Map<Integer, MonthlyWeeksRateResponse> currentMonthlyStatistic;

    private MonthlyWeekTasksStatisticResponse() {
    }

    public MonthlyWeekTasksStatisticResponse(
        final LocalDate startDate,
        final LocalDate endDate,
        final Tasks tasks
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.beforeMonthlyStatistic = new HashMap<>();
        this.currentMonthlyStatistic = new HashMap<>();
        calculate(tasks);
    }

    private void calculate(final Tasks tasks) {
        final Map<Integer, List<TaskJpaEntity>> beforeMonthlyWeekTaskMap = tasks.groupByWeekOfMonth(startDate);
        final Map<Integer, List<TaskJpaEntity>> currentMonthlyWeekTaskMap = tasks.groupByWeekOfMonth(endDate);

        for (int i = 0; i < 5; i++) {
            final List<TaskJpaEntity> beforeMonthTask = beforeMonthlyWeekTaskMap.getOrDefault(i, emptyList());
            final List<TaskJpaEntity> currentMonthTask = currentMonthlyWeekTaskMap.getOrDefault(i, emptyList());
            beforeMonthlyStatistic.put(i, new MonthlyWeeksRateResponse(beforeMonthTask));
            currentMonthlyStatistic.put(i, new MonthlyWeeksRateResponse(currentMonthTask));
        }
    }
}
