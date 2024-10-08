package project.dailyge.app.core.retrospect.application.command;

import java.time.LocalDateTime;

public record RetrospectUpdateCommand(
    String title,
    String content,
    LocalDateTime date,
    boolean isPublic
) {

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\",\"isPublic\":\"%s\"}",
            title, content, date, isPublic
        );
    }
}
