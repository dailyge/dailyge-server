package project.dailyge.entity.user;

import static java.time.LocalDateTime.now;
import project.dailyge.entity.common.DomainEvent;
import project.dailyge.entity.common.EventType;

public class UserEvent extends DomainEvent {

    private UserEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType,
        final int publishCount
    ) {
        super(publisher, domain, eventId, eventType, publishCount, now());
        validate(publisher, eventId, eventType);

    }

    private UserEvent(
        final Long publisher,
        final String domain,
        final String eventId,
        final EventType eventType
    ) {
        this(publisher, domain, eventId, eventType, 1);
    }

    public static UserEvent createEvent(
        final Long publisher,
        final String eventId,
        final EventType eventType
    ) {
        validate(publisher, eventId, eventType);
        return new UserEvent(
            publisher,
            "users",
            eventId,
            eventType
        );
    }

    public static UserEvent createEventWithIncreasedPublishCount(final UserEvent event) {
        return new UserEvent(
            event.getPublisher(),
            event.getDomain(),
            event.getEventId(),
            event.getEventType(),
            event.getPublishCount() + 1
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
        return getEventType().name();
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
        return getPublishCount() >= maxPublishCount;
    }

    public boolean isType(final EventType eventType) {
        return getEventType().equals(eventType);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"publisher\":\"%s\",\"domain\":\"%s\",\"eventId\":\"%s\",\"eventType\":\"%s\",\"createdAt\":\"%s\"}",
            getPublisher(), getDomain(), getEventId(), getEventType(), getCreatedAt()
        );
    }
}
