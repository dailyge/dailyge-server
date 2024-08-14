package project.dailyge.app.test.user.unittest;

import static java.time.LocalDateTime.now;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import project.dailyge.app.core.user.event.UserEventListener;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import project.dailyge.document.event.EventDocument;
import project.dailyge.document.event.EventDocumentWriteRepository;
import project.dailyge.entity.common.EventPublisher;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.user.UserEvent;

@DisplayName("[UnitTest] UserEventListener 단위 테스트")
class UserEventListenerUnitTest {

    private EventPublisher<UserEvent> eventPublisher;
    private EventDocumentWriteRepository eventWriteRepository;
    private UserEventListener userEventListener;

    @BeforeEach
    void setUp() {
        eventPublisher = mock(EventPublisher.class);
        eventWriteRepository = mock(EventDocumentWriteRepository.class);
        userEventListener = new UserEventListener(eventPublisher, eventWriteRepository);
    }

    @Test
    @DisplayName("이벤트를 수신하면 EventDocument로 변환해 저장한다.")
    void whenEventReceivedThenConvertAndSave() {
        final String eventId = createTimeBasedUUID();
        final Long publisherId = 1L;
        final EventDocument eventDocument = new EventDocument(
            eventId, publisherId, "user", CREATE.name(), now()
        );
        when(eventWriteRepository.save(eventDocument))
            .thenReturn(eventId);

        final UserEvent event = UserEvent.createEvent(publisherId, eventId, CREATE);
        userEventListener.listenEvent(event);

        verify(eventWriteRepository)
            .save(eventDocument);
    }

    @Test
    @DisplayName("예외 발생 시 이벤트를 다시 발행한다.")
    void whenExceptionThrownThenPublishInternalEvent() {
        final String eventId = createTimeBasedUUID();
        final Long publisherId = 1L;
        final UserEvent event = UserEvent.createEvent(publisherId, eventId, CREATE);

        doThrow(new RuntimeException("Database error"))
            .when(eventWriteRepository).save(any(EventDocument.class));

        userEventListener.listenEvent(event);

        verify(eventPublisher)
            .publishInternalEvent(event);
    }
}
