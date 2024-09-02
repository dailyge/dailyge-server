package project.dailyge.entity.task;

import static java.time.LocalDateTime.now;
import lombok.Getter;
import project.dailyge.entity.common.Event;
import project.dailyge.entity.common.EventType;

import java.time.LocalDate;

@Getter
public class TaskEvent extends Event {

    /**
     * 직렬화를 위한 생성자로 외부에서 호출하지 말 것.
     */
    private TaskEvent() {
    }

    private TaskEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType,
        final int publishCount
    ) {
        validate(publisher, eventId, eventType);
        this.publisher = publisher;
        this.domain = domain;
        this.eventId = eventId;
        this.eventType = eventType;
        this.publishCount = publishCount;
        this.createdAt = now();
    }

    private TaskEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
        this(publisher, domain, eventId, eventType, 1);
    }

    public static TaskEvent createEvent(
        final Long publisher,
        final String eventId,
        final EventType eventType
    ) {
        validate(publisher, eventId, eventType);
        return new TaskEvent(
            publisher,
            "task",
            eventId,
            eventType
        );
    }

    public static TaskEvent createEventWithIncreasedPublishCount(final TaskEvent event) {
        return new TaskEvent(
            event.publisher,
            event.getDomain(),
            event.eventId,
            event.getEventType(),
            event.publishCount + 1
        );
    }

    public static void validate(
        final Long publisher,
        final String eventId,
        final EventType eventType
    ) {
        if (publisher == null) {
            throw new IllegalArgumentException(INVALID_PUBLISHER_ID_ERROR_MESSAGE);
        }
        if (eventId == null) {
            throw new IllegalArgumentException(INVALID_EVENT_ID_ERROR_MESSAGE);
        }
        if (eventType == null) {
            throw new IllegalArgumentException(INVALID_EVENT_TYPE_ERROR_MESSAGE);
        }
    }

    public String getEventTypeAsString() {
        return eventType.name();
    }

    public LocalDate getLocalDate() {
        return createdAt.toLocalDate();
    }

    public String getInvalidPublisherIdErrorMessage() {
        return INVALID_PUBLISHER_ID_ERROR_MESSAGE;
    }

    public String getInvalidEventIdErrorMessage() {
        return INVALID_EVENT_ID_ERROR_MESSAGE;
    }

    public String getInvalidEventTypeErrorMessage() {
        return INVALID_EVENT_TYPE_ERROR_MESSAGE;
    }

    public boolean overCount(final int maxPublishCount) {
        return publishCount >= maxPublishCount;
    }

    public boolean isType(final EventType eventType) {
        return this.eventType.equals(eventType);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"publisher\":\"%s\",\"domain\":\"%s\",\"eventId\":\"%s\",\"eventType\":\"%s\",\"createdAt\":\"%s\"}",
            publisher, domain, eventId, eventType, createdAt
        );
    }
}
