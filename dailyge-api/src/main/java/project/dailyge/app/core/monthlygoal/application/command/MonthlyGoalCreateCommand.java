package project.dailyge.app.core.monthlygoal.application.command;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.monthlygoal.MonthlyGoalJpaEntity;

import java.time.LocalDate;

public record MonthlyGoalCreateCommand(
    String title,
    String content,
    LocalDate date
) {
    public MonthlyGoalJpaEntity toEntity(final DailygeUser dailygeUser) {
        return new MonthlyGoalJpaEntity(title, content, date, dailygeUser.getId());
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\"}",
            title != null ? title : "",
            content != null ? content : "",
            date
        );
    }
}
