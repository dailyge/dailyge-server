package project.dailyge.app.test.event.unittest;

import static java.util.concurrent.TimeUnit.SECONDS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import project.dailyge.app.core.event.application.EventWriteUseCase;
import project.dailyge.app.core.user.event.UserEventListener;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.document.event.EventDocumentWriteRepository;
import project.dailyge.entity.common.EventPublisher;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.user.UserEvent;

import java.util.concurrent.CountDownLatch;

@DisplayName("[UnitTest] 이벤트 쓰기 단위 테스트")
class EventLayerWriteUnitTest {

    @Mock
    private UserFacade userFacade;

    @Mock
    private EventDocumentWriteRepository eventDocumentWriteRepository;

    @Mock
    private EventWriteUseCase eventWriteUseCase;

    @Mock
    private EventPublisher<UserEvent> userEventPublisher;

    private UserEventListener eventListener;

    @BeforeEach
    void setUp() {
        openMocks(this);
        eventListener = new UserEventListener(userFacade, userEventPublisher, eventDocumentWriteRepository);
    }

    @Test
    @DisplayName("예외가 발생하면 이벤트를 다시 발행한다.")
    void whenExceptionOccursThenPublishInternalEvent() throws InterruptedException {
        final String eventId = "test-event-id";
        final Long publisherId = 1L;
        final UserEvent event = UserEvent.createEvent(publisherId, eventId, CREATE);

        doThrow(new RuntimeException("Exception"))
            .when(eventDocumentWriteRepository).save(any());

        final CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(userEventPublisher).publishInternalEvent(any(UserEvent.class));

        eventListener.listenInternalEvent(event);
        latch.await(3, SECONDS);

        verify(userEventPublisher).publishInternalEvent(argThat(argument -> !argument.equals(event)));
    }

    @Test
    @DisplayName("MAX_PUBLISH_COUNT 이하일 때 이벤트가 다시 발행된다.")
    void whenPublishCountDoesNotExceedMaxThenRepublishEvent() throws InterruptedException {
        final String eventId = "test-event-id";
        final Long publisherId = 1L;
        final UserEvent event = UserEvent.createEvent(publisherId, eventId, CREATE);

        final CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(eventDocumentWriteRepository).save(any());

        eventListener.listenInternalEvent(event);
        eventListener.listenInternalEvent(event);

        latch.await(3, SECONDS);

        verify(eventWriteUseCase, never()).saveDeadLetter(any(UserEvent.class));
        verify(eventDocumentWriteRepository, atLeastOnce()).save(any());
    }
}
