package project.dailyge.app.user.application;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;
import project.dailyge.core.cache.user.UserCacheReadUseCase;

@ApplicationLayer
@RequiredArgsConstructor
public class UserCacheReadService implements UserCacheReadUseCase {

    private final UserCacheReadRepository userCacheReadRepository;

    @Override
    public UserCache findById(final Long userId) {
        return userCacheReadRepository.findById(userId);
    }

    @Override
    public boolean existsById(final Long userId) {
        return userCacheReadRepository.existsById(userId);
    }
}
