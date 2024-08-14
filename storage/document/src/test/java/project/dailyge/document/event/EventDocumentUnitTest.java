package project.dailyge.document.event;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] EventDocument 단위 테스트")
class EventDocumentUnitTest {

    private static final String EVENT_ID = createTimeBasedUUID();
    private static final Long PUBLISHER = 1L;
    private static final String DOMAIN = "user";
    private static final String EVENT_TYPE = "CREATED";
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private EventDocument eventDocument;

    @BeforeEach
    void setUp() {
        eventDocument = new EventDocument(
            EVENT_ID,
            PUBLISHER,
            DOMAIN,
            EVENT_TYPE,
            CREATED_AT
        );
    }

    @Test
    @DisplayName("EventDocument 객체를 생성하면, 필드 값이 정상적으로 초기화 된다.")
    void whenCreateEventDocumentThenFieldsShouldBeSetCorrectly() {
        assertAll(
            () -> assertEquals(EVENT_ID, eventDocument.getEventId()),
            () -> assertEquals(PUBLISHER, eventDocument.getPublisher()),
            () -> assertEquals(DOMAIN, eventDocument.getDomain()),
            () -> assertEquals(EVENT_TYPE, eventDocument.getEventType()),
            () -> assertEquals(CREATED_AT, eventDocument.getCreatedAt())
        );
    }

    @Test
    @DisplayName("동일한 eventId를 가진 객체는 equals 메서드에서 true를 반환한다.")
    void whenEventIdIsSameThenEqualsShouldReturnTrue() {
        final EventDocument sameEvent = new EventDocument(
            EVENT_ID,
            eventDocument.publisher,
            eventDocument.domain,
            eventDocument.eventType,
            eventDocument.createdAt
        );

        assertEquals(eventDocument, sameEvent);
    }

    @Test
    @DisplayName("서로 다른 eventId를 가진 객체는 equals 메서드에서 false를 반환한다.")
    void whenEventIdIsDifferentThenEqualsShouldReturnFalse() {
        final EventDocument differentEvent = new EventDocument(
            createTimeBasedUUID(),
            eventDocument.publisher,
            eventDocument.domain,
            eventDocument.eventType,
            eventDocument.createdAt
        );
        assertNotEquals(eventDocument, differentEvent);
    }

    @Test
    @DisplayName("hashCode 메서드는 eventId를 기반으로 동일한 값을 반환한다.")
    void whenEventIdIsSameThenHashCodeShouldBeEqual() {
        final EventDocument otherEvent = new EventDocument(
            EVENT_ID,
            1L,
            "user",
            "VIEW",
            LocalDateTime.now()
        );
        assertEquals(eventDocument.hashCode(), otherEvent.hashCode());
    }
}
