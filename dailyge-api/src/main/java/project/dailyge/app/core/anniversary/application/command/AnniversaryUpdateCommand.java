package project.dailyge.app.core.anniversary.application.command;

import java.time.LocalDateTime;

public record AnniversaryUpdateCommand(
    String name,
    LocalDateTime date,
    boolean remind,
    Long emojiId
) {

    @Override
    public String toString() {
        return String.format("{\"name\":\"%s\", \"date\":\"%s\", \"remind\":%b, \"emojiId\":%d}", name, date, remind, emojiId);
    }
}
