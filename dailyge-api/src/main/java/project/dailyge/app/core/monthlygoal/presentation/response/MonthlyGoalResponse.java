package project.dailyge.app.core.monthlygoal.presentation.response;

import project.dailyge.entity.goal.MonthlyGoalJpaEntity;

public class MonthlyGoalResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final boolean done;
    private final int year;
    private final int month;
    private final Long userId;
    private final String createdAt;

    public MonthlyGoalResponse(final MonthlyGoalJpaEntity monthlyGoal) {
        this.id = monthlyGoal.getId();
        this.title = monthlyGoal.getTitle();
        this.content = monthlyGoal.getContent();
        this.done = monthlyGoal.isDone();
        this.year = monthlyGoal.getYear();
        this.month = monthlyGoal.getMonth();
        this.userId = monthlyGoal.getUserId();
        this.createdAt = monthlyGoal.getCreatedAtAsString();
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

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
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
            "{\"id\": %d, \"title\": \"%s\", \"content\": \"%s\", \"done\": %b, \"year\": %d, \"month\": %d, \"userId\": %d, \"createdAt\": \"%s\"}",
            id, title, content, done, year, month, userId, createdAt
        );
    }
}
