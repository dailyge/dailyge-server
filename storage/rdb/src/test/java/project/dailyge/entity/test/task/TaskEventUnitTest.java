package project.dailyge.entity.test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.common.EventType;
import project.dailyge.entity.task.TaskEvent;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] TaskEvent 단위 테스트")
class TaskEventUnitTest {

    @Test
    @DisplayName("올바른 인자로 TaskEvent를 생성할 수 있다.")
    void whenValidParamsThenCreateTaskEvent() {
        final Long publisher = 1L;
        final String domain = "task";
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent = TaskEvent.createEvent(publisher, domain, eventId, eventType);

        assertAll(
            () -> assertThat(taskEvent.getPublisher()).isEqualTo(publisher),
            () -> assertThat(taskEvent.getDomain()).isEqualTo(domain),
            () -> assertThat(taskEvent.getEventId()).isEqualTo(eventId),
            () -> assertThat(taskEvent.getEventType()).isEqualTo(eventType),
            () -> assertThat(taskEvent.getCreatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("DOMAIN이 자동 할당되어 TaskEvent를 생성할 수 있다.")
    void whenValidParamsWithDomainThenCreateTaskEvent() {
        final Long publisher = 1L;
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent = TaskEvent.createEvent(publisher, eventId, eventType);

        assertAll(
            () -> assertThat(taskEvent.getPublisher()).isEqualTo(publisher),
            () -> assertThat(taskEvent.getDomain()).isEqualTo("task"),
            () -> assertThat(taskEvent.getEventId()).isEqualTo(eventId),
            () -> assertThat(taskEvent.getEventType()).isEqualTo(eventType),
            () -> assertThat(taskEvent.getCreatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("publisher가 null이면 IllegalArgumentException이 발생한다.")
    void whenPublisherIsNullThenThrowIllegalArgumentException() {
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        assertThatThrownBy(() -> TaskEvent.createEvent(null, eventId, eventType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 publisherId를 입력해주세요:null");
    }

    @Test
    @DisplayName("domain이 올바르지 않으면 IllegalArgumentException이 발생한다.")
    void whenDomainIsInvalidThenThrowIllegalArgumentException() {
        final Long publisher = 1L;
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        assertThatThrownBy(() -> TaskEvent.createEvent(publisher, "invalid_domain", eventId, eventType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 도메인을 입력해주세요:invalid_domain");
    }

    @Test
    @DisplayName("eventId가 null 또는 공백이면 IllegalArgumentException이 발생한다.")
    void whenEventIdIsInvalidThenThrowIllegalArgumentException() {
        final Long publisher = 1L;
        final EventType eventType = EventType.CREATE;

        assertThatThrownBy(() -> TaskEvent.createEvent(publisher, null, eventType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 eventId를 입력해주세요:null");

        assertThatThrownBy(() -> TaskEvent.createEvent(publisher, "", eventType))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 eventId를 입력해주세요:");
    }

    @Test
    @DisplayName("eventType이 null이면 IllegalArgumentException이 발생한다.")
    void whenEventTypeIsNullThenThrowIllegalArgumentException() {
        final Long publisher = 1L;
        final String eventId = "event123";

        assertThatThrownBy(() -> TaskEvent.createEvent(publisher, eventId, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 eventType을 입력해주세요:null");
    }

    @Test
    @DisplayName("toString 메서드는 정의한 형식대로 JSON 문자열을 반환한다.")
    void whenToStringThenReturnJsonString() {
        final Long publisher = 1L;
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent = TaskEvent.createEvent(publisher, eventId, eventType);

        final String expectedString = "{"
            + "\"eventId\":\"" + eventId + "\","
            + "\"eventType\":\"" + eventType + "\","
            + "\"publisher\":\"" + publisher + "\","
            + "\"domain\":\"task\","
            + "\"createdAt\":\"" + taskEvent.getCreatedAt() + "\""
            + "}";

        assertThat(taskEvent.toString()).hasToString(expectedString);
    }

    @Test
    @DisplayName("같은 ID를 가진 TaskEvent 객체는 동일한 해시코드를 가진다.")
    void whenSameIdThenHashCodeIsEqual() {
        final Long publisher = 1L;
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent1 = TaskEvent.createEvent(publisher, eventId, eventType);
        final TaskEvent taskEvent2 = TaskEvent.createEvent(publisher, eventId, eventType);

        assertThat(taskEvent1.hashCode()).hasSameHashCodeAs(taskEvent2.hashCode());
    }

    @Test
    @DisplayName("ID가 같으면 같은 객체로 여긴다.")
    void whenSameIdThenEqualsIsTrue() {
        final Long publisher = 1L;
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent1 = TaskEvent.createEvent(publisher, eventId, eventType);
        final TaskEvent taskEvent2 = TaskEvent.createEvent(publisher, eventId, eventType);

        assertThat(taskEvent1).isEqualTo(taskEvent2);
    }

    @Test
    @DisplayName("ID가 다르면 다른 객체로 여긴다.")
    void whenDifferentIdThenEqualsIsFalse() {
        final Long publisher = 1L;
        final String eventId1 = "event123";
        final String eventId2 = "event124";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent1 = TaskEvent.createEvent(publisher, eventId1, eventType);
        final TaskEvent taskEvent2 = TaskEvent.createEvent(publisher, eventId2, eventType);

        assertThat(taskEvent1).isNotEqualTo(taskEvent2);
    }

    @Test
    @DisplayName("TaskEvent의 값이 바뀌지 않음을 확인한다.")
    void whenCreateTaskEventThenValuesAreNotChanged() {
        final Long publisher = 1L;
        final String eventId = "event123";
        final EventType eventType = EventType.CREATE;

        final TaskEvent taskEvent = TaskEvent.createEvent(publisher, eventId, eventType);
        final Long originalPublisher = taskEvent.getPublisher();
        final String originalEventId = taskEvent.getEventId();
        final EventType originalEventType = taskEvent.getEventType();
        final String originalDomain = taskEvent.getDomain();
        final LocalDateTime originalCreatedAt = taskEvent.getCreatedAt();

        assertAll(
            () -> assertEquals(originalPublisher, taskEvent.getPublisher()),
            () -> assertEquals(originalEventId, taskEvent.getEventId()),
            () -> assertEquals(originalEventType, taskEvent.getEventType()),
            () -> assertEquals(originalDomain, taskEvent.getDomain()),
            () -> assertEquals(originalCreatedAt, taskEvent.getCreatedAt())
        );
    }

    @Test
    @DisplayName("동일한 객체를 비교할 때 true를 반환한다.")
    void whenSameInstanceThenEqualsIsTrue() {
        final TaskEvent taskEvent = TaskEvent.createEvent(1L, "event123", EventType.CREATE);
        assertThat(taskEvent).isEqualTo(taskEvent);
    }

    @Test
    @DisplayName("다른 타입의 객체를 비교할 때 false를 반환한다.")
    void whenDifferentClassThenEqualsIsFalse() {
        final TaskEvent taskEvent = TaskEvent.createEvent(1L, "event123", EventType.CREATE);
        assertThat(taskEvent).isNotEqualTo(new Object());
    }

    @Test
    @DisplayName("null과 비교할 때 false를 반환한다.")
    void whenCompareWithNullThenEqualsIsFalse() {
        final TaskEvent taskEvent = TaskEvent.createEvent(1L, "event123", EventType.CREATE);
        assertThat(taskEvent).isNotEqualTo(null);
    }

    @Test
    @DisplayName("eventId가 동일한 다른 TaskEvent와 비교할 때 true를 반환한다.")
    void whenSameEventIdThenEqualsIsTrue() {
        final TaskEvent taskEvent1 = TaskEvent.createEvent(1L, "event123", EventType.CREATE);
        final TaskEvent taskEvent2 = TaskEvent.createEvent(2L, "event123", EventType.UPDATE);
        assertEquals(taskEvent1, taskEvent2);
    }

    @Test
    @DisplayName("eventId가 다른 TaskEvent와 비교할 때 false를 반환한다.")
    void whenDifferentEventIdThenEqualsIsFalse() {
        final TaskEvent taskEvent1 = TaskEvent.createEvent(1L, "event123", EventType.CREATE);
        final TaskEvent taskEvent2 = TaskEvent.createEvent(2L, "event124", EventType.UPDATE);
        assertNotEquals(taskEvent1, taskEvent2);
    }
}
