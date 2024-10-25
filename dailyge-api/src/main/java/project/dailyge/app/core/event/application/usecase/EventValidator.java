package project.dailyge.app.core.event.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.event.exception.EventTypeException;
import project.dailyge.app.core.event.persistence.LocalEventCache;

import static project.dailyge.app.core.event.exception.EventCodeAndMessage.EVENT_NOT_FOUND;
import static project.dailyge.app.core.event.exception.EventCodeAndMessage.INVALID_EVENT;

@Component
@RequiredArgsConstructor
public class EventValidator {

    private final LocalEventCache localEventCache;

    public void validate(final Long eventId) {
        if (!localEventCache.exists(eventId)) {
            throw EventTypeException.from(EVENT_NOT_FOUND);
        }
        if (localEventCache.isExpired(eventId)) {
            throw EventTypeException.from(INVALID_EVENT);
        }
    }
}
