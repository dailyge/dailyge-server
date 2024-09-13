package project.dailyge.app.core.task.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import project.dailyge.app.common.annotation.EventLayer;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.document.event.EventDocument;
import project.dailyge.document.event.EventDocumentWriteRepository;
import project.dailyge.entity.common.EventPublisher;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.task.TaskEvent;
import static project.dailyge.entity.task.TaskEvent.createEventWithIncreasedPublishCount;

@EventLayer(value = "TaskEventListener")
@RequiredArgsConstructor
public class TaskEventListener {

    private final TaskFacade taskFacade;
    private final EventPublisher<TaskEvent> taskEventPublisher;
    private final EventDocumentWriteRepository eventWriteRepository;

    @Async
    @EventListener
    public void execute(final TaskEvent event) {
        try {
            if (event.isType(CREATE)) {
                final EventDocument eventDocument = createEventDocument(event);
                eventWriteRepository.save(eventDocument);
                taskFacade.createMonthlyTasksInternally(event.getPublisher(), event.getLocalDate());
            }
        } catch (Exception ex) {
            taskEventPublisher.publishInternalEvent(createEventWithIncreasedPublishCount(event));
        }
    }

    private static EventDocument createEventDocument(final TaskEvent event) {
        return new EventDocument(
            event.getEventId(),
            event.getPublisher(),
            event.getDomain(),
            event.getEventTypeAsString(),
            event.getCreatedAt()
        );
    }
}
