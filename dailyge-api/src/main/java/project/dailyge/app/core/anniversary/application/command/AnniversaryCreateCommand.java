package project.dailyge.app.core.anniversary.application.command;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDateTime;

public record AnniversaryCreateCommand(
    String name,
    LocalDateTime date,
    boolean remind,
    Long emojiId
) {

    public AnniversaryJpaEntity toEntity(final DailygeUser dailygeUser) {
        return new AnniversaryJpaEntity(name, date, remind, emojiId, dailygeUser.getUserId());
    }

    @Override
    public String toString() {
        return String.format("{\"name\":\"%s\", \"date\":\"%s\", \"remind\":%b, \"emojiId\":%d}", name, date, remind, emojiId);
    }
}
