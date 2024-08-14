package project.dailyge.app.core.event.application;

import project.dailyge.entity.common.Event;

public interface EventWriteUseCase {
    void save(Event event);
}
