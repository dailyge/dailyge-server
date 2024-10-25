package project.dailyge.app.core.task.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import project.dailyge.app.common.annotation.EventLayer;
import project.dailyge.app.core.event.application.EventWriteService;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.task.TaskEvent;

@EventLayer
@RequiredArgsConstructor
public class TaskEventPublisher implements EventPublisher<TaskEvent> {

    private static final int MAX_PUBLISH_COUNT = 3;
    private final ApplicationEventPublisher eventPublisher;
    private final EventWriteService eventWriteService;

    @Async
    @Override
    public void publishInternalEvent(final TaskEvent event) {
        if (event.overCount(MAX_PUBLISH_COUNT)) {
            eventWriteService.saveDeadLetter(event);
            return;
        }
        eventPublisher.publishEvent(event);
    }
}
