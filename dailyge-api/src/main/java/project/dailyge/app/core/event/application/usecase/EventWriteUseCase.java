package project.dailyge.app.core.event.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.event.application.EventWriteService;
import project.dailyge.app.core.event.persistence.DeadLetterQueue;
import project.dailyge.entity.common.DomainEvent;

@ApplicationLayer
public class EventWriteUseCase implements EventWriteService {

    private final DeadLetterQueue inMemoryDeadLetterQueue;

    public EventWriteUseCase(final DeadLetterQueue inMemoryDeadLetterQueue) {
        this.inMemoryDeadLetterQueue = inMemoryDeadLetterQueue;
    }

    @Override
    public void saveDeadLetter(final DomainEvent event) {
        inMemoryDeadLetterQueue.save(event);
    }
}
