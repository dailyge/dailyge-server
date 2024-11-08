package project.dailyge.app.core.user.external.cache;

import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import project.dailyge.app.common.annotation.ExternalLayer;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.app.core.user.persistence.UserReadDao;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;
import project.dailyge.core.cache.user.UserCacheReadService;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_SERVICE_UNAVAILABLE;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.UPDATE;
import static project.dailyge.entity.user.UserEvent.createEvent;

@ExternalLayer(value = "UserCacheReadUseCase")
class UserCacheReadUseCase implements UserCacheReadService {

    private final UserCacheReadRepository userCacheReadRepository;
    private final UserReadDao userReadDao;
    private final ApplicationEventPublisher eventPublisher;

    public UserCacheReadUseCase(
        final UserCacheReadRepository userCacheReadRepository,
        final UserReadDao userReadDao,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.userCacheReadRepository = userCacheReadRepository;
        this.userReadDao = userReadDao;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserCache findById(final Long userId) {
        final UserCache userCache = userCacheReadRepository.findById(userId);
        if (userCache != null) {
            return userCache;
        }
        final Optional<UserJpaEntity> findUser = userReadDao.findById(userId);
        if (findUser.isPresent()) {
            final UserJpaEntity user = findUser.get();
            if (user.isBlacklist()) {
                throw UserTypeException.from(USER_SERVICE_UNAVAILABLE);
            }
            final UserEvent event = createEvent(userId, createTimeBasedUUID(), UPDATE);
            eventPublisher.publishEvent(event);
            return new UserCache(userId, user.getNickname(), user.getEmail(), user.getProfileImageUrl(), user.getRoleAsString());
        }
        throw UserTypeException.from(USER_NOT_FOUND);
    }

    @Override
    public boolean existsById(final Long userId) {
        return userCacheReadRepository.existsById(userId);
    }
}
