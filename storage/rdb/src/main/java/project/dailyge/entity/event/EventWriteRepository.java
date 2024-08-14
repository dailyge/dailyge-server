package project.dailyge.entity.event;

import project.dailyge.entity.common.Event;

public interface EventWriteRepository {
    void save(Event event);
}
