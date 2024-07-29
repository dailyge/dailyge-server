package project.dailyge.entity.task;

import static java.time.LocalDateTime.now;
import lombok.Getter;
import project.dailyge.entity.common.Event;
import project.dailyge.entity.common.EventType;

@Getter
public class TaskEvent extends Event {

    private static final String DOMAIN = "task";
    private String eventId;
    private EventType eventType;

    private TaskEvent() {
    }

    private TaskEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
//        validate(publisher, domain, eventId, eventType);
        this.publisher = publisher;
        this.domain = domain;
        this.eventId = eventId;
        this.eventType = eventType;
        this.createdAt = now();
    }

    public static TaskEvent createEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
        return new TaskEvent(publisher, domain, eventId, eventType);
    }

    public static TaskEvent createEvent(
        final Long publisher,
        final String eventId,
        final EventType eventType
    ) {
        return new TaskEvent(publisher, DOMAIN, eventId, eventType);
    }

    private void validate(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
        if (publisher == null) {

        }
        if (!DOMAIN.equals(domain)) {

        }
        if (eventId == null || eventId.isBlank()) {

        }
    }

    public TaskEvent(
        final Long publisher,
        final String eventId,
        final EventType eventType
    ) {
        validate(publisher, DOMAIN, eventId, eventType);
        this.publisher = publisher;
        this.domain = DOMAIN;
        this.eventId = eventId;
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "{" +
            "\"eventId\":\"" + eventId + "\"," +
            "\"eventType\":\"" + eventType + "\"," +
            "\"publisher\":\"" + publisher + "\"," +
            "\"domain\":\"" + domain + "\"," +
            "\"createdAt\":\"" + createdAt + "\"" +
            "}";
    }
}
