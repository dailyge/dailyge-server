package project.dailyge.app.core.user.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import project.dailyge.app.common.annotation.EventLayer;
import project.dailyge.app.core.event.application.EventWriteUseCase;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEventWithIncreasedPublishCount;

@EventLayer
@RequiredArgsConstructor
public class UserEventPublisher implements EventPublisher<UserEvent> {

    private static final int MAX_PUBLISH_COUNT = 3;
    private final ApplicationEventPublisher eventPublisher;
    private final EventWriteUseCase eventWriteUseCase;

    @Override
    public void publishInternalEvent(final UserEvent event) {
        if (event.overCount(MAX_PUBLISH_COUNT)) {
            eventWriteUseCase.save(event);
            return;
        }
        eventPublisher.publishEvent(createEventWithIncreasedPublishCount(event));
    }

    // TODO. 추후 구현
    @Override
    public void publishExternalEvent(final UserEvent event) {

    }
}
