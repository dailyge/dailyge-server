package project.dailyge.app.test.event.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.event.application.service.EventValidator;
import project.dailyge.app.core.event.exception.EventTypeException;
import project.dailyge.app.core.event.persistence.EventCache;
import project.dailyge.app.core.event.persistence.LocalEventCache;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static project.dailyge.app.core.event.exception.EventCodeAndMessage.EVENT_NOT_FOUND;
import static project.dailyge.app.core.event.exception.EventCodeAndMessage.INVALID_EVENT;

@DisplayName("[단위테스트] Event Validator 단위테스트")
public class EventValidatorUnitTest {

    private LocalEventCache localEventCache;
    private EventValidator eventValidator;

    @BeforeEach
    void setUp() {
        localEventCache = new LocalEventCache();
        eventValidator = new EventValidator(localEventCache);
    }

    @Test
    @DisplayName("존재하지 않는 이벤트이면 EventTypeExcpetion을 던진다.")
    void whenEventIdDoesNotExistsThenThrowEventTypeException() {
        final Long notFoundEventId = Long.MAX_VALUE;
        assertThrows(EventTypeException.from(EVENT_NOT_FOUND).getClass(), () -> eventValidator.validate(notFoundEventId));
    }

    @Test
    @DisplayName("이미 지난 이벤트이면 EventTypeException을 던진다.")
    void whenPastEventThenThrowEventTypeException() {
        final EventCache pastEventCache = new EventCache(
            2L,
            LocalDateTime.now().minusHours(4),
            LocalDateTime.now().minusHours(2),
            0L
        );
        localEventCache.save(pastEventCache);
        assertThrows(EventTypeException.from(INVALID_EVENT).getClass(), () -> eventValidator.validate(pastEventCache.getId()));
    }

    @Test
    @DisplayName("시작하지 않은 이벤트이면 EventTypeException을 던진다.")
    void whenEventDoesNotStartThenThrowEventTypeException() {
        final EventCache futureEventCache = new EventCache(
            3L,
            LocalDateTime.now().plusHours(3),
            LocalDateTime.now().plusHours(6),
            0L
        );
        localEventCache.save(futureEventCache);
        assertThrows(EventTypeException.from(INVALID_EVENT).getClass(), () -> eventValidator.validate(futureEventCache.getId()));
    }

    @Test
    @DisplayName("진행중인 이벤트이면 EventTypeException을 던지지 않는다.")
    void whenEventInProceedThenThrowEventTypeException() {
        final EventCache eventCache = new EventCache(
            1L,
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now().plusHours(3),
            0L
        );
        localEventCache.save(eventCache);
        assertDoesNotThrow(() -> eventValidator.validate(eventCache.getId()));
    }
}
