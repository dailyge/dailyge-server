package project.dailyge.app.core.weeklygoal.presentation.response;

import project.dailyge.entity.goal.WeeklyGoalJpaEntity;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isDone() {
        return done;
    }

    public LocalDateTime getStartWeekDate() {
        return startWeekDate;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"title\":\"%s\",\"content\":\"%s\",\"done\":\"%s\",\"startWeekDate\":\"%s\",\"userId\":\"%s\",\"createdAt\":\"%s\"}",
            id, title, content, done, startWeekDate, userId, createdAt
        );
    }
}
