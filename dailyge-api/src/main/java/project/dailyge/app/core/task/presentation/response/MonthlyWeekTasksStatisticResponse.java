package project.dailyge.app.core.task.presentation.response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.Tasks;
import static java.util.Collections.emptyList;

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
        this.beforeMonthlyStatistic = calculate(tasks, startDate);
        this.currentMonthlyStatistic = calculate(tasks, endDate);
    }

    private Map<Integer, MonthlyWeeksRateResponse> calculate(
        final Tasks tasks,
        final LocalDate date
    ) {
        final Map<Integer, List<TaskJpaEntity>> monthlyWeekTaskMap = tasks.groupByWeekOfMonth(date);
        final Map<Integer, MonthlyWeeksRateResponse> monthlyStatisticGroup = new HashMap<>();
        for (int week = 0; week < 5; week++) {
            final List<TaskJpaEntity> beforeMonthTask = monthlyWeekTaskMap.getOrDefault(week, emptyList());
            monthlyStatisticGroup.put(week, new MonthlyWeeksRateResponse(beforeMonthTask));
        }
        return monthlyStatisticGroup;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Map<Integer, MonthlyWeeksRateResponse> getBeforeMonthlyStatistic() {
        return beforeMonthlyStatistic;
    }

    public Map<Integer, MonthlyWeeksRateResponse> getCurrentMonthlyStatistic() {
        return currentMonthlyStatistic;
    }
}
