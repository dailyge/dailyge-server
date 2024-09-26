package project.dailyge.app.core.task.presentation.response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;
import project.dailyge.entity.task.Tasks;

@Getter
public class MonthlyWeeksStatisticResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private Map<Integer, MonthlyWeeksRateResponse> beforeMonthlyStatistic;
    private Map<Integer, MonthlyWeeksRateResponse> currentMonthlyStatistic;

    private MonthlyWeeksStatisticResponse() {
    }

    public MonthlyWeeksStatisticResponse(
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

    public Map<LocalDate, MonthlyWeeksRateResponse> calculate(final Tasks tasks) {
        final Map<Integer, List<TaskJpaEntity>> beforeMonthTaskMap = getWeeklyTaskCounts(tasks.monthTaskGroupByDate(startDate));
        final Map<Integer, List<TaskJpaEntity>> currentMonthTaskMap = getWeeklyTaskCounts(tasks.monthTaskGroupByDate(endDate));

        for (int i = 0; i < 5; i++) {
            calculateWeekly(beforeMonthTaskMap.getOrDefault(i, List.of()), beforeMonthlyStatistic, i);
            calculateWeekly(currentMonthTaskMap.getOrDefault(i, List.of()), currentMonthlyStatistic, i);
        }
        return null;
    }

    private Map<Integer, List<TaskJpaEntity>> getWeeklyTaskCounts(final Map<LocalDate, List<TaskJpaEntity>> beforeMonthTaskMap) {
        final Map<Integer, List<TaskJpaEntity>> tasksGroupByWeekMap = new HashMap<>();
        for (final LocalDate date : beforeMonthTaskMap.keySet()) {
            int weekOfMonth = getWeekOfMonth(date) == 0 ? 1 : getWeekOfMonth(date);
            tasksGroupByWeekMap.computeIfAbsent(weekOfMonth, k -> new ArrayList<>()).addAll(beforeMonthTaskMap.get(date));
        }
        return tasksGroupByWeekMap;
    }

    private int getWeekOfMonth(final LocalDate date) {
        final WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        return date.get(weekFields.weekOfMonth());
    }

    private void calculateWeekly(
        final List<TaskJpaEntity> weekTasks,
        final Map<Integer, MonthlyWeeksRateResponse> targetMap,
        final int weekOfMonth
    ) {
        if (weekTasks == null || weekTasks.isEmpty()) {
            targetMap.put(weekOfMonth, new MonthlyWeeksRateResponse(0.0, 0.0));
            return;
        }
        final double beforeMonthlySuccessRate = Tasks.calculatePercentage(weekTasks, TaskStatus.DONE);
        targetMap.put(weekOfMonth, new MonthlyWeeksRateResponse(beforeMonthlySuccessRate, 100.0 - beforeMonthlySuccessRate));
    }
}
