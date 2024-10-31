package project.dailyge.app.core.event.application;

import project.dailyge.entity.common.DomainEvent;

public interface EventWriteService {
    void saveDeadLetter(DomainEvent event);
}
