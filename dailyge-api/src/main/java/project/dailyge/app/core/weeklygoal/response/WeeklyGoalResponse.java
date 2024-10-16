package project.dailyge.app.core.weeklygoal.response;

import lombok.Getter;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;

@Getter
public class WeeklyGoalResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final boolean done;
    private final LocalDateTime startWeekDate;
    private final Long userId;
    private final String createdAt;

    public WeeklyGoalResponse(final WeeklyGoalJpaEntity weeklyGoal) {
        this.id = weeklyGoal.getId();
        this.title = weeklyGoal.getTitle();
        this.content = weeklyGoal.getContent();
        this.done = weeklyGoal.isDone();
        this.startWeekDate = weeklyGoal.getWeekStartDate();
        this.userId = weeklyGoal.getUserId();
        this.createdAt = weeklyGoal.getCreatedAtAsString();
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"title\":\"%s\",\"content\":\"%s\",\"done\":\"%s\",\"startWeekDate\":\"%s\",\"userId\":\"%s\",\"createdAt\":\"%s\"}",
            id, title, content, done, startWeekDate, userId, createdAt
        );
    }
}
