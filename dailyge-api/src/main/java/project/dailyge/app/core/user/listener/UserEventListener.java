package project.dailyge.app.core.user.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final UserCacheWriteUseCase userCacheWriteUseCase;
    private final UserReadUseCase userReadUseCase;
    private final UserWriteUseCase userWriteUseCase;

    @Async
    @EventListener
    public void listenEvent(final UserEvent event) {
        final UserCache findUserCache = userCacheReadUseCase.findById(event.getPublisher());
        if (findUserCache == null) {
            renewUserCache(event);
            return;
        }
        if (!userReadUseCase.existsById(event.getPublisher())) {
            saveUser(findUserCache);
        }
        userCacheWriteUseCase.refreshExpirationDate(event.getPublisher());
    }

    private void renewUserCache(final UserEvent event) {
        final UserJpaEntity user = userReadUseCase.findById(event.getPublisher());
        final UserCache userCache = new UserCache(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getRoleToString()
        );
        userCacheWriteUseCase.save(userCache);
    }

    private void saveUser(final UserCache findUserCache) {
        final UserJpaEntity user = new UserJpaEntity(
            findUserCache.getId(),
            findUserCache.getNickname(),
            findUserCache.getEmail()
        );
        userWriteUseCase.save(user);
    }
}
