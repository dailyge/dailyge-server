package project.dailyge.app.core.task.presentation.response;

import java.time.LocalDate;
import java.util.List;
import project.dailyge.entity.task.Tasks;
import static project.dailyge.entity.task.Tasks.calculateMonthlyRanks;

public class MonthlyTasksStatisticResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<Double> beforeMonthlyStatistic;
    private List<Double> currentMonthlyStatistic;

    private MonthlyTasksStatisticResponse() {
    }

    public MonthlyTasksStatisticResponse(
        final LocalDate startDate,
        final LocalDate endDate,
        final Tasks tasks
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        beforeMonthlyStatistic = calculate(tasks, startDate);
        currentMonthlyStatistic = calculate(tasks, endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<Double> getBeforeMonthlyStatistic() {
        return beforeMonthlyStatistic;
    }

    public List<Double> getCurrentMonthlyStatistic() {
        return currentMonthlyStatistic;
    }

    private List<Double> calculate(
        final Tasks tasks,
        final LocalDate date
    ) {
        final List<Integer> monthlyRankCounts = tasks.countMonthTasksByRank(date);
        return calculateMonthlyRanks(monthlyRankCounts);
    }
}
