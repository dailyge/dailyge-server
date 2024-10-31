package project.dailyge.app.core.event.application.usecase;

import org.springframework.stereotype.Component;
import static project.dailyge.app.core.event.exception.EventCodeAndMessage.EVENT_NOT_FOUND;
import static project.dailyge.app.core.event.exception.EventCodeAndMessage.INVALID_EVENT;
import project.dailyge.app.core.event.exception.EventTypeException;
import project.dailyge.app.core.event.persistence.LocalEventCache;

@Component
public class EventValidator {

    private final LocalEventCache localEventCache;

    public EventValidator(final LocalEventCache localEventCache) {
        this.localEventCache = localEventCache;
    }

    public void validate(final Long eventId) {
        if (!localEventCache.exists(eventId)) {
            throw EventTypeException.from(EVENT_NOT_FOUND);
        }
        if (localEventCache.isExpired(eventId)) {
            throw EventTypeException.from(INVALID_EVENT);
        }
    }
}
