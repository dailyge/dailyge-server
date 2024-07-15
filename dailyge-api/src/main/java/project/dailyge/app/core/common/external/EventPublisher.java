package project.dailyge.app.core.common.external;

import project.dailyge.entity.common.Event;

public interface EventPublisher {
    void publishExternalEvent(Event event);
}
