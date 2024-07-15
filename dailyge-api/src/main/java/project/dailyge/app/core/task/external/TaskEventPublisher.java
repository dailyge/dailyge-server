package project.dailyge.app.core.task.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.common.external.EventPublisher;
import project.dailyge.entity.common.Event;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventPublisher implements EventPublisher {

    // TODO. 다음 PR에서 개발 예정
    @Async
    @Override
    public void publishExternalEvent(final Event event) {
        try {

        } catch (Exception ex) {

        }
    }
}
