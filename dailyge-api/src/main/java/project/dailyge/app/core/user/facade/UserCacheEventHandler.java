package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.dailyge.core.cache.user.UserCacheEvent;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;

@Component
@RequiredArgsConstructor
public class UserCacheEventHandler {

    private final UserCacheWriteUseCase userCacheWriteUseCase;

    @Async
    @EventListener
    public void onUserCacheEvent(final UserCacheEvent event) {
        userCacheWriteUseCase.save(event.userCache());
    }
}
