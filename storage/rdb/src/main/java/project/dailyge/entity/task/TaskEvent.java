package project.dailyge.entity.task;

import static java.time.LocalDateTime.now;
import lombok.Getter;
import project.dailyge.entity.common.Event;
import project.dailyge.entity.common.EventType;

import java.util.Objects;

@Getter
public class TaskEvent extends Event {

    private static final String DOMAIN = "task";

    /**
     * 직렬화를 위한 생성자로 외부에서 호출하지 말 것.
     */
    private TaskEvent() {
    }

    private TaskEvent(
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
            final String message = String.format("올바른 publisherId를 입력해주세요:%d", publisher);
            throw new IllegalArgumentException(message);
        }
        if (!DOMAIN.equals(domain)) {
            final String message = String.format("올바른 도메인을 입력해주세요:%s", domain);
            throw new IllegalArgumentException(message);
        }
        if (eventId == null || eventId.isBlank()) {
            final String message = String.format("올바른 eventId를 입력해주세요:%s", eventId);
            throw new IllegalArgumentException(message);
        }
        if (eventType == null) {
            final String message = String.format("올바른 eventType을 입력해주세요:%s", eventType);
            throw new IllegalArgumentException(message);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TaskEvent taskEvent = (TaskEvent) obj;
        return Objects.equals(eventId, taskEvent.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "{\"publisher\":\"" + publisher + "\"," +
            "\"domain\":\"" + domain + "\"," +
            "\"eventId\":\"" + eventId + "\"," +
            "\"eventType\":\"" + eventType + "\"," +
            "\"createdAt\":\"" + createdAt + "\"}";
    }
}
