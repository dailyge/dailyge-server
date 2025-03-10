package project.dailyge.app.core.user.event;

import org.springframework.context.event.EventListener;
import project.dailyge.app.common.annotation.EventLayer;
import project.dailyge.app.core.user.facade.UserFacade;
import project.dailyge.document.event.EventDocument;
import project.dailyge.document.event.EventDocumentWriteRepository;
import project.dailyge.entity.common.EventPublisher;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEventWithIncreasedPublishCount;

@EventLayer(value = "UserEventListener")
public class UserEventListener {

    private final UserFacade userFacade;
    private final EventPublisher<UserEvent> userEventPublisher;
    private final EventDocumentWriteRepository eventWriteRepository;

    public UserEventListener(
        final UserFacade userFacade,
        final EventPublisher<UserEvent> userEventPublisher,
        final EventDocumentWriteRepository eventWriteRepository
    ) {
        this.userFacade = userFacade;
        this.userEventPublisher = userEventPublisher;
        this.eventWriteRepository = eventWriteRepository;
    }

    @EventListener
    public void listenInternalEvent(final UserEvent event) {
        try {
            if (event.isType(CREATE)) {
                eventWriteRepository.save(createEventDocument(event));
            }
            userFacade.saveCache(event);
        } catch (Exception ex) {
            userEventPublisher.publishInternalEvent(createEventWithIncreasedPublishCount(event));
        }
    }

    private static EventDocument createEventDocument(final UserEvent event) {
        return new EventDocument(
            event.getEventId(),
            event.getPublisher(),
            event.getDomain(),
            event.getEventTypeAsString(),
            event.getCreatedAt()
        );
    }
}
