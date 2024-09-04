package project.dailyge.app.core.user.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import project.dailyge.app.common.annotation.EventLayer;
import project.dailyge.app.core.event.application.EventWriteUseCase;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.user.UserEvent;

@EventLayer
@RequiredArgsConstructor
public class UserEventPublisher implements EventPublisher<UserEvent> {

    private static final int MAX_PUBLISH_COUNT = 3;
    private final ApplicationEventPublisher eventPublisher;
    private final EventWriteUseCase eventWriteUseCase;

    @Async
    @Override
    public void publishInternalEvent(final UserEvent event) {
        if (event.overCount(MAX_PUBLISH_COUNT)) {
            eventWriteUseCase.saveDeadLetter(event);
            return;
        }
        eventPublisher.publishEvent(event);
    }
}
