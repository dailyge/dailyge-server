package project.dailyge.app.core.weeklygoal.application.command;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.weeklygoal.WeeklyGoalJpaEntity;

import java.time.LocalDate;

public record WeeklyGoalCreateCommand(
    String title,
    String content,
    LocalDate date
) {

    public WeeklyGoalJpaEntity toEntity(final DailygeUser dailygeUser) {
        return new WeeklyGoalJpaEntity(
            title,
            content,
            date.atTime(0, 0, 0, 0),
            dailygeUser.getId()
        );
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\"}",
            title, content, date
        );
    }
}
