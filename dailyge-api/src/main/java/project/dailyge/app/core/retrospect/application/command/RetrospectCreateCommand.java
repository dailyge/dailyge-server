package project.dailyge.app.core.retrospect.application.command;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

import java.time.LocalDateTime;

public record RetrospectCreateCommand(
    String title,
    String content,
    LocalDateTime date,
    boolean isPublic
) {

    public RetrospectJpaEntity toEntity(final DailygeUser dailygeUser) {
        return new RetrospectJpaEntity(title, content, date, isPublic, dailygeUser.getUserId());
    }
}
