package project.dailyge.app.core.event.application;

import project.dailyge.entity.common.Event;

public interface EventWriteUseCase {
    void saveDeadLetter(Event event);
}
