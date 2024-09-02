package project.dailyge.app.core.monthlygoal.presentation.response;

import lombok.Getter;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

@Getter
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

    @Override
    public String toString() {
        return String.format(
            "{\"id\": %d, \"title\": \"%s\", \"content\": \"%s\", \"done\": %b, \"year\": %d, \"month\": %d, \"userId\": %d, \"createdAt\": \"%s\"}",
            id, title, content, done, year, month, userId, createdAt
        );
    }
}
