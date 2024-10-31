package project.dailyge.app.core.task.presentation.response;

import project.dailyge.entity.task.TaskJpaEntity;
import static project.dailyge.entity.task.TaskStatus.DONE;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;
import static project.dailyge.entity.task.TaskStatus.TODO;
import static project.dailyge.entity.task.Tasks.calculatePercentage;

import java.util.List;

public class TaskAchievementRateResponse {

    private static final int PERCENTAGE = 100;

    private double todo;
    private double inProgress;
    private double done;

    private TaskAchievementRateResponse() {
    }

    public TaskAchievementRateResponse(final List<TaskJpaEntity> tasks) {
        if (tasks.isEmpty()) {
            this.todo = 0;
            this.inProgress = 0;
            this.done = 0;
            return;
        }
        this.todo = calculatePercentage(tasks, TODO);
        this.inProgress = calculatePercentage(tasks, IN_PROGRESS);
        this.done = calculatePercentage(tasks, DONE);

        final double totalPercentage = this.todo + this.inProgress + this.done;
        if (totalPercentage != PERCENTAGE) {
            this.done += PERCENTAGE - totalPercentage;
        }
    }

    public double getTodo() {
        return todo;
    }

    public double getInProgress() {
        return inProgress;
    }

    public double getDone() {
        return done;
    }
}
