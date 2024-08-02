package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.core.user.application.UserCacheWriteUseCase;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;

@Service
@RequiredArgsConstructor
class UserCacheWriteService implements UserCacheWriteUseCase {

    private final UserCacheWriteRepository userWriteRepository;

    @Override
    public void save(final UserCache userCache) {
        userWriteRepository.save(userCache);
    }
}
