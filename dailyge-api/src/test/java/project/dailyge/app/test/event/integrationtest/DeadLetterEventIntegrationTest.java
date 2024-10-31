package project.dailyge.app.test.event.integrationtest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.event.application.EventWriteService;
import project.dailyge.app.core.event.persistence.DeadLetterQueue;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import project.dailyge.entity.common.DomainEvent;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.user.UserEvent.createEvent;

@DisplayName("[IntegrationTest] 이벤트 쓰기 통합 테스트")
class DeadLetterEventIntegrationTest extends DatabaseTestBase {

    @Autowired
    private EventWriteService eventWriteService;

    @Autowired
    private DeadLetterQueue deadLetterQueue;

    @Test
    @DisplayName("유실된 이벤트를 저장하면 In-Memory 데드레터에 저장된다.")
    void whenWriteEventThenSavedInMemoryDeadLetter() {
        final DomainEvent event = createEvent(1L, createTimeBasedUUID(), CREATE);
        eventWriteService.saveDeadLetter(event);

        assertFalse(deadLetterQueue.isEmpty());
    }
}
