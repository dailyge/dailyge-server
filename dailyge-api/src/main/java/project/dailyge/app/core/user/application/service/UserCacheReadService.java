package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;

@Service
@RequiredArgsConstructor
class UserCacheReadService implements UserCacheReadUseCase {

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
