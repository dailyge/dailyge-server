package project.dailyge.app.core.retrospect.application.command;

import java.time.LocalDateTime;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

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
