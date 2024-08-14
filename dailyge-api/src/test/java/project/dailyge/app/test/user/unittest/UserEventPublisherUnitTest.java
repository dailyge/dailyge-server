package project.dailyge.app.test.user.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.context.ApplicationEventPublisher;
import project.dailyge.app.core.event.application.EventWriteUseCase;
import project.dailyge.app.core.user.event.UserEventPublisher;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEvent;
import static project.dailyge.entity.user.UserEvent.createEventWithIncreasedPublishCount;

@DisplayName("[UnitTest] 사용자 이벤트 발행 단위 테스트")
class UserEventPublisherUnitTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private EventWriteUseCase eventWriteUseCase;

    private UserEventPublisher userEventPublisher;

    @BeforeEach
    void setUp() {
        openMocks(this);
        userEventPublisher = new UserEventPublisher(eventPublisher, eventWriteUseCase);
    }

    @Test
    @DisplayName("발행 횟수가 최대치를 초과했을 때 데드레터 큐에 저장된다.")
    void whenPublishCountExceedsMaxThenSave() {
        final String eventId = createTimeBasedUUID();
        final Long publisherId = 1L;
        final UserEvent firstEvent = createEvent(publisherId, eventId, CREATE);

        final UserEvent secondEvent = createEventWithIncreasedPublishCount(firstEvent);
        final UserEvent thirdEvent = createEventWithIncreasedPublishCount(secondEvent);

        userEventPublisher.publishInternalEvent(thirdEvent);

        verify(eventWriteUseCase, times(1))
            .save(thirdEvent);
        verify(eventPublisher, never())
            .publishEvent(any());
    }

    @Test
    @DisplayName("발행 횟수가 최대치를 초과하지 않았을 때 이벤트가 다시 발행된다.")
    void whenPublishCountDoesNotExceedMaxThenRepublishEvent() {
        final String eventId = createTimeBasedUUID();
        final Long publisherId = 1L;
        final UserEvent event = createEvent(publisherId, eventId, CREATE);

        userEventPublisher.publishInternalEvent(event);

        verify(eventWriteUseCase, never()).save(any());
        verify(eventPublisher, times(1)).publishEvent(any(UserEvent.class));
    }
}
