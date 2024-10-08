package project.dailyge.app.core.event.application;

import project.dailyge.entity.common.Event;

public interface EventWriteService {
    void saveDeadLetter(Event event);
}
