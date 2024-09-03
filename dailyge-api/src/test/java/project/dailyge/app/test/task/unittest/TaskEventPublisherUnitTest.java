package project.dailyge.app.test.task.unittest;

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
import project.dailyge.app.core.task.event.TaskEventPublisher;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.task.TaskEvent;
import static project.dailyge.entity.task.TaskEvent.createEvent;
import static project.dailyge.entity.task.TaskEvent.createEventWithIncreasedPublishCount;

@DisplayName("[UnitTest] 사용자 이벤트 발행 단위 테스트")
class TaskEventPublisherUnitTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private EventWriteUseCase eventWriteUseCase;

    private TaskEventPublisher taskEventPublisher;

    @BeforeEach
    void setUp() {
        openMocks(this);
        taskEventPublisher = new TaskEventPublisher(eventPublisher, eventWriteUseCase);
    }

    @Test
    @DisplayName("발행 횟수가 최대치를 초과했을 때 데드레터 큐에 저장된다.")
    void whenPublishCountExceedsMaxThenSave() {
        final String eventId = createTimeBasedUUID();
        final Long publisherId = 1L;
        final TaskEvent firstEvent = createEvent(publisherId, eventId, CREATE);

        final TaskEvent secondEvent = createEventWithIncreasedPublishCount(firstEvent);
        final TaskEvent thirdEvent = createEventWithIncreasedPublishCount(secondEvent);

        taskEventPublisher.publishInternalEvent(thirdEvent);

        verify(eventWriteUseCase, times(1))
            .saveDeadLetter(thirdEvent);
        verify(eventPublisher, never())
            .publishEvent(any());
    }

    @Test
    @DisplayName("발행 횟수가 최대치를 초과하지 않았을 때 이벤트가 다시 발행된다.")
    void whenPublishCountDoesNotExceedMaxThenRepublishEvent() {
        final String eventId = createTimeBasedUUID();
        final Long publisherId = 1L;
        final TaskEvent event = createEvent(publisherId, eventId, CREATE);

        taskEventPublisher.publishInternalEvent(event);

        verify(eventWriteUseCase, never()).saveDeadLetter(any());
        verify(eventPublisher, times(1)).publishEvent(any(TaskEvent.class));
    }
}
