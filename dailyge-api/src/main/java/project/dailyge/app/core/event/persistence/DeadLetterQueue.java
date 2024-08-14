package project.dailyge.app.core.event.persistence;

import org.springframework.stereotype.Component;
import project.dailyge.entity.common.Event;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class DeadLetterQueue {

    private static final Queue<Event> inMemoryDeadLetter = new LinkedBlockingQueue<>();

    public void save(final Event event) {
        inMemoryDeadLetter.add(event);
    }

    public boolean isEmpty() {
        return inMemoryDeadLetter.isEmpty();
    }
}
