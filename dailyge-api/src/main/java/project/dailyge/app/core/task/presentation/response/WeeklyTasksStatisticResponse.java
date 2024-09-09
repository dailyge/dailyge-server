package project.dailyge.app.core.task.presentation.response;

import static java.util.Collections.emptyList;
import lombok.Getter;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.Tasks;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WeeklyTasksStatisticResponse {

    private String startDate;
    private String endDate;
    private List<String> keys;
    private Map<String, TaskAchievementRateResponse> statistics;

    private WeeklyTasksStatisticResponse() {
    }

    public WeeklyTasksStatisticResponse(
        final LocalDate startDate,
        final LocalDate endDate,
        final Tasks tasks
    ) {
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
        this.keys = tasks.getKeysAsString();
        this.statistics = calculate(tasks.groupByDate());
    }

    private Map<String, TaskAchievementRateResponse> calculate(final Map<String, List<TaskJpaEntity>> tasksGroupByDateMap) {
        final Map<String, TaskAchievementRateResponse> statistics = new HashMap<>();
        for (final String date : tasksGroupByDateMap.keySet()) {
            final List<TaskJpaEntity> tasks = tasksGroupByDateMap.getOrDefault(date, emptyList());
            statistics.put(date, new TaskAchievementRateResponse(tasks));
        }
        return statistics;
    }
}
