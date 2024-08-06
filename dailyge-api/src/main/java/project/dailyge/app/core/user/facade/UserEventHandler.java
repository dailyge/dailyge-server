package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.user.UserEvent;

@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final UserCacheWriteUseCase userCacheWriteUseCase;

    @Async
    @EventListener
    public void onUserEvent(final UserEvent event) {
        final UserCache userCache = new UserCache(event.getPublisher(), event.getNickname(), event.getEmail(), event.getProfileImageUrl());
        userCacheWriteUseCase.save(userCache);
    }
}
