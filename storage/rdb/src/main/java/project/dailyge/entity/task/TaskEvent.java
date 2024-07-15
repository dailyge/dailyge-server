package project.dailyge.entity.task;

import lombok.Getter;
import project.dailyge.entity.common.Event;
import project.dailyge.entity.common.EventType;

import java.util.Arrays;
import java.util.List;

@Getter
public class TaskEvent extends Event {

    private static final String DOMAIN = "task";
    private static final List<EventType> eventTypes = Arrays.stream(EventType.values())
        .toList();

    private final String eventId;
    private final EventType eventType;

    public TaskEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
        validate(publisher, domain, eventId, eventType);
        this.publisher = publisher;
        this.domain = domain;
        this.eventId = eventId;
        this.eventType = eventType;
    }

    public static Event createEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
        return new TaskEvent(publisher, domain, eventId, eventType);
    }

    public static Event createEvent(
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
        if (!eventTypes.contains(eventType)) {

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
}
