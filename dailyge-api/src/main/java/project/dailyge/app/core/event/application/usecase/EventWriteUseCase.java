package project.dailyge.app.core.event.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.event.application.EventWriteService;
import project.dailyge.app.core.event.persistence.DeadLetterQueue;
import project.dailyge.entity.common.Event;

@ApplicationLayer
@RequiredArgsConstructor
public class EventWriteUseCase implements EventWriteService {

    private final DeadLetterQueue inMemoryDeadLetterQueue;

    @Override
    public void saveDeadLetter(final Event event) {
        inMemoryDeadLetterQueue.save(event);
    }
}
