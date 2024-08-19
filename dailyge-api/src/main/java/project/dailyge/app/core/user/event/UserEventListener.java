package project.dailyge.app.core.user.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import project.dailyge.app.common.annotation.EventLayer;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.document.event.EventDocument;
import project.dailyge.document.event.EventDocumentWriteRepository;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEventWithIncreasedPublishCount;

@EventLayer
@RequiredArgsConstructor
public class UserEventListener {

    private final UserFacade userFacade;
    private final EventPublisher<UserEvent> eventPublisher;
    private final EventDocumentWriteRepository eventWriteRepository;

    @Async
    @EventListener
    public void listenInternalEvent(final UserEvent event) {
        try {
            userFacade.saveCache(event);
            final EventDocument eventDocument = new EventDocument(
                event.getEventId(),
                event.getPublisher(),
                event.getDomain(),
                event.getEventTypeAsString(),
                event.getCreatedAt()
            );
            eventWriteRepository.save(eventDocument);
        } catch (Exception ex) {
            eventPublisher.publishInternalEvent(createEventWithIncreasedPublishCount(event));
        }
    }
}
