package project.dailyge.app.core.user.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;
import project.dailyge.core.cache.user.UserCacheReadService;

@ApplicationLayer
public class UserCacheReadUseCase implements UserCacheReadService {

    private final UserCacheReadRepository userCacheReadRepository;

    public UserCacheReadUseCase(final UserCacheReadRepository userCacheReadRepository) {
        this.userCacheReadRepository = userCacheReadRepository;
    }

    @Override
    public UserCache findById(final Long userId) {
        return userCacheReadRepository.findById(userId);
    }

    @Override
    public boolean existsById(final Long userId) {
        return userCacheReadRepository.existsById(userId);
    }
}
