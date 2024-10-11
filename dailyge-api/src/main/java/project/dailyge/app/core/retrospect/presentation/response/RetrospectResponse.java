package project.dailyge.app.core.retrospect.presentation.response;

import java.time.LocalDateTime;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":\"%s\",\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\",\"isPublic\":\"%s\"}",
            id, title, content, date, isPublic
        );
    }
}
