package project.dailyge.app.core.task.presentation.response;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;
import project.dailyge.entity.task.Tasks;
import static project.dailyge.entity.task.TaskAchievementRank.getAchievementRank;
import static project.dailyge.entity.task.Tasks.calculatePercentage;

@Getter
public class MonthlyTasksStatisticResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private double[] beforeMonthlyStatistic;
    private double[] currentMonthlyStatistic;

    private MonthlyTasksStatisticResponse() {
    }

    public MonthlyTasksStatisticResponse(
        final LocalDate startDate,
        final LocalDate endDate,
        final Tasks tasks
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.beforeMonthlyStatistic = new double[5];
        this.currentMonthlyStatistic = new double[5];
        calculate(tasks);
    }

    private void calculate(final Tasks tasks) {
        final int[] beforeMonthlyCounts = getCountsByRank(tasks.monthTaskGroupByDate(startDate));
        final int[] currentMonthlyCounts = getCountsByRank(tasks.monthTaskGroupByDate(endDate));

        calculateMonthlyStatistic(beforeMonthlyCounts, beforeMonthlyStatistic);
        calculateMonthlyStatistic(currentMonthlyCounts, currentMonthlyStatistic);
    }

    private int[] getCountsByRank(final Map<LocalDate, List<TaskJpaEntity>> beforeMonthlyTaskMap) {
        if (beforeMonthlyTaskMap.isEmpty()) {
            return new int[0];
        }
        final int[] countsByRank = new int[5];
        for (final LocalDate date : beforeMonthlyTaskMap.keySet()) {
            final List<TaskJpaEntity> tasksByDate = beforeMonthlyTaskMap.get(date);
            final double successRateByDate = calculatePercentage(tasksByDate, TaskStatus.DONE);
            final int achievementRank = getAchievementRank(successRateByDate);
            countsByRank[achievementRank]++;
        }
        return countsByRank;
    }

    private void calculateMonthlyStatistic(
        final int[] monthlyCounts,
        final double... targetStatistic
    ) {
        if (monthlyCounts.length == 0) {
            return;
        }
        final int beforeMonthlySum = Arrays.stream(monthlyCounts).sum();
        for (int i = monthlyCounts.length - 1; i > 0; i--) {
            targetStatistic[i] = calculatePercentage(monthlyCounts[i], beforeMonthlySum);
        }
        targetStatistic[0] = 100.0 - Arrays.stream(targetStatistic).sum();
    }
}
