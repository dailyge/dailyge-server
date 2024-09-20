package project.dailyge.app.core.event.exception;

import lombok.Getter;
import project.dailyge.app.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

import static project.dailyge.app.core.event.exception.EventCodeAndMessage.*;

@Getter
public sealed class EventTypeException extends BusinessException {

    private static final Map<EventCodeAndMessage, EventTypeException> factory = new HashMap<>();

    private EventTypeException(final EventCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(INVALID_EVENT, new InvalidEventException(INVALID_EVENT));
        factory.put(EVENT_NOT_FOUND, new EventNotFoundException(EVENT_NOT_FOUND));
        factory.put(EVENT_UN_RESOLVED_EXCEPTION, new EventUnResolvedException(EVENT_UN_RESOLVED_EXCEPTION));
    }

    public static EventTypeException from(final EventCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    private static EventTypeException getException(final EventCodeAndMessage codeAndMessage) {
        final EventTypeException eventTypeException = factory.get(codeAndMessage);
        if (eventTypeException != null) {
            return eventTypeException;
        }
        return factory.get(EVENT_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class EventNotFoundException extends EventTypeException {
        private EventNotFoundException(final EventCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class InvalidEventException extends EventTypeException {
        private InvalidEventException(final EventCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class EventUnResolvedException extends EventTypeException {
        private EventUnResolvedException(final EventCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
