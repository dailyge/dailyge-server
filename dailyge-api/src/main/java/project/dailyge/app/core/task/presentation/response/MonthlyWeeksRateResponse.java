package project.dailyge.app.core.task.presentation.response;

import java.util.List;
import lombok.Getter;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskStatus;
import static project.dailyge.entity.task.Tasks.calculatePercentage;

@Getter
public class MonthlyWeeksRateResponse {

    private double successRate;
    private double failedRate;

    private MonthlyWeeksRateResponse() {
    }

    public MonthlyWeeksRateResponse(final List<TaskJpaEntity> weekTasks) {
        if (weekTasks.isEmpty()) {
            this.successRate = 0.0;
            this.failedRate = 0.0;
            return;
        }
        final double weekSuccessRate = calculatePercentage(weekTasks, TaskStatus.DONE);
        this.successRate = weekSuccessRate;
        this.failedRate = 100.0 - weekSuccessRate;
    }
}
