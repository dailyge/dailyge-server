package project.dailyge.app.core.event.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.event.application.EventWriteUseCase;
import project.dailyge.app.core.event.persistence.DeadLetterQueue;
import project.dailyge.entity.common.Event;

@ApplicationLayer
@RequiredArgsConstructor
public class EventWriteService implements EventWriteUseCase {

    private final DeadLetterQueue inMemoryDeadLetterQueue;

    @Override
    public void save(final Event event) {
        inMemoryDeadLetterQueue.save(event);
    }
}
