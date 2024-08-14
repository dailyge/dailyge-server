package project.dailyge.document.event;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import project.dailyge.document.DocumentBaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Document(collection = "events")
public class EventDocument extends DocumentBaseEntity {

    @Field(name = "_id")
    protected String eventId;

    @Field(name = "publisher")
    protected Long publisher;

    @Field(name = "domain")
    protected String domain;

    @Field(name = "event_type")
    protected String eventType;

    @Field(name = "created_at")
    protected LocalDateTime createdAt;

    /**
     * 직렬화를 위한 기본 생성자로, 외부 패키지에서 호출하지 말 것.
     */
    private EventDocument() {
    }

    public EventDocument(
        final String eventId,
        final Long publisher,
        final String domain,
        final String eventType,
        final LocalDateTime createdAt
    ) {
        this.eventId = eventId;
        this.publisher = publisher;
        this.domain = domain;
        this.eventType = eventType;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final EventDocument that = (EventDocument) obj;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
