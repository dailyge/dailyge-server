package project.dailyge.app.core.task.presentation.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import project.dailyge.entity.task.Tasks;
import static project.dailyge.entity.task.Tasks.calculateMonthlyRanks;

@Getter
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
        calculate(tasks);
    }

    private void calculate(final Tasks tasks) {
        final List<Integer> beforeMonthlyRankCounts = tasks.monthTaskRankCounts(startDate);
        final List<Integer> afterMonthlyRankCounts = tasks.monthTaskRankCounts(endDate);

        beforeMonthlyStatistic = calculateMonthlyRanks(beforeMonthlyRankCounts);
        currentMonthlyStatistic = calculateMonthlyRanks(afterMonthlyRankCounts);
    }
}
