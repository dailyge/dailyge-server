package project.dailyge.entity.test.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.entity.common.EventType;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.task.TaskEvent;
import static project.dailyge.entity.task.TaskEvent.createEvent;
import static project.dailyge.entity.task.TaskEvent.createEventWithIncreasedPublishCount;

import java.util.UUID;

@DisplayName("[UnitTest] TaskEvent 단위 테스트")
class TaskEventUnitTest {

    private static final long PUBLISHER = 1L;
    private static final String EVENT_ID = UUID.randomUUID().toString();

    private TaskEvent event;

    @BeforeEach
    void setUp() {
        event = createEvent(
            PUBLISHER,
            EVENT_ID,
            CREATE
        );
    }

    @Test
    @DisplayName("Task 이벤트를 생성하면, 정상적으로 생성된다.")
    void whenCreateTaskEventThenEventShouldBeGeneratedNormally() {
        assertAll(
            () -> assertEquals(PUBLISHER, event.getPublisher()),
            () -> assertEquals(EVENT_ID, event.getEventId()),
            () -> assertEquals(CREATE, event.getEventType()),
            () -> assertEquals("CREATE", event.getEventTypeAsString()),
            () -> assertEquals("task", event.getDomain())
        );
    }

    @ParameterizedTest
    @DisplayName("이벤트 타입을 체크할 수 있다.")
    @ValueSource(strings = {"CREATE", "UPDATE", "DELETE"})
    void whenDetermineEventTypeThenCanCheckEventType(final String parameter) {
        final TaskEvent newEvent = createEvent(1L, UUID.randomUUID().toString(), EventType.valueOf(parameter));
        assertTrue(newEvent.isType(EventType.valueOf(parameter)));
    }

    @Test
    @DisplayName("publishCount가 증가된 새로운 이벤트를 생성할 수 있다.")
    void whenCreateEventWithIncreasedPublishCountThenCountShouldBeIncremented() {
        final int initialPublishCount = event.getPublishCount();
        final TaskEvent newEvent = createEventWithIncreasedPublishCount(event);

        assertAll(
            () -> assertThat(newEvent).isNotNull(),
            () -> assertThat(newEvent.getPublisher()).isEqualTo(event.getPublisher()),
            () -> assertThat(newEvent.getEventId()).isEqualTo(event.getEventId()),
            () -> assertThat(newEvent.getEventType()).isEqualTo(event.getEventType()),
            () -> assertThat(newEvent.getDomain()).isEqualTo(event.getDomain()),
            () -> assertThat(newEvent.getPublishCount()).isEqualTo(initialPublishCount + 1)
        );
    }

    @Test
    @DisplayName("publishCount가 maxPublishCount를 초과하지 않은 경우, false를 반환한다.")
    void whenPublishCountIsLessThanMaxPublishCountThenReturnFalse() {
        final int maxPublishCount = event.getPublishCount() + 1;
        final boolean result = event.overCount(maxPublishCount);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("publisher가 비어있을 경우, IllegalArgumentException이 발생한다.")
    void whenPublisherIsNullThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> createEvent(
            null,
            EVENT_ID,
            CREATE
        )).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(event.getInvalidPublisherIdErrorMessage());
    }

    @Test
    @DisplayName("eventId가 비어있을 경우, IllegalArgumentException이 발생한다.")
    void whenEventIdIsNullThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> createEvent(
            PUBLISHER,
            null,
            CREATE
        )).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(event.getInvalidEventIdErrorMessage());
    }

    @Test
    @DisplayName("eventType이 비어있을 경우, IllegalArgumentException이 발생한다.")
    void whenEventTypeIsNullThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> createEvent(
            PUBLISHER,
            EVENT_ID,
            null
        )).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(event.getInvalidEventTypeErrorMessage());
    }

    @Test
    @DisplayName("toString 메서드는 정의한 형식대로 JSON 문자열을 반환한다.")
    void whenToStringThenReturnJsonString() {
        final String expectedString = String.format(
            "{\"publisher\":\"%s\",\"domain\":\"%s\",\"eventId\":\"%s\",\"eventType\":\"%s\",\"createdAt\":\"%s\"}",
            PUBLISHER, "task", EVENT_ID, CREATE, event.getCreatedAt()
        );

        assertThat(event.toString()).hasToString(expectedString);
    }
}
