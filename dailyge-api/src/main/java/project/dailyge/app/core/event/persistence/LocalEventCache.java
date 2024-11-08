package project.dailyge.app.core.event.persistence;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocalEventCache {

    private final Map<Long, EventCache> cacheMap = new HashMap<>();

    public void save(final EventCache eventCache) {
        cacheMap.put(eventCache.getId(), eventCache);
    }

    public boolean exists(final Long eventId) {
        return cacheMap.containsKey(eventId);
    }

    public boolean isExpired(final Long eventId) {
        final EventCache eventCache = cacheMap.get(eventId);
        final LocalDateTime now = LocalDateTime.now();
        return !(now.isAfter(eventCache.getStartTime()) && now.isBefore(eventCache.getEndTime()));
    }
}
