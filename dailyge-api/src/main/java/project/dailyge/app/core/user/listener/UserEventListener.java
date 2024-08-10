package project.dailyge.app.core.user.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final UserWriteUseCase userWriteUseCase;

    @Async
    @EventListener
    public void listenEvent(final UserEvent event) {
        final UserCache userCache = userCacheReadUseCase.findById(event.getPublisher());
        final UserJpaEntity user = new UserJpaEntity(
            userCache.getId(),
            userCache.getNickname(),
            userCache.getEmail()
        );
        userWriteUseCase.save(user);
    }
}
