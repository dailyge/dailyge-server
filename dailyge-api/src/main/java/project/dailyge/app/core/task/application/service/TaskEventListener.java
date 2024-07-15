package project.dailyge.app.core.task.application.service;

import org.springframework.stereotype.Component;
import project.dailyge.app.core.common.event.EventListener;
import project.dailyge.entity.common.Event;

@Component
class TaskEventListener implements EventListener {

    @Override
    public void listen(final Event event) {

    }
}
