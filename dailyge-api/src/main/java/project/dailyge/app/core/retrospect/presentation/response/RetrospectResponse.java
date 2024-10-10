package project.dailyge.app.core.retrospect.presentation.response;

import java.time.LocalDateTime;
import lombok.Getter;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@Getter
public class RetrospectResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime date;
    private final boolean isPublic;

    public RetrospectResponse(final RetrospectJpaEntity retrospect) {
        this.id = retrospect.getId();
        this.title = retrospect.getTitle();
        this.content = retrospect.getContent();
        this.date = retrospect.getDate();
        this.isPublic = retrospect.isPublic();
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\",\"isPublic\":\"%s\"}",
            id, title, content, date, isPublic
        );
    }
}
