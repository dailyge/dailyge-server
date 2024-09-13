package project.dailyge.app.core.user.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;

@RequiredArgsConstructor
@ApplicationLayer(value = "UserCacheWriteService")
public class UserCacheWriteService implements UserCacheWriteUseCase {

    private final UserCacheWriteRepository userWriteRepository;

    @Override
    public void save(final UserCache userCache) {
        userWriteRepository.save(userCache);
    }

    @Override
    public void refreshExpirationDate(final Long userId) {
        userWriteRepository.refreshExpirationDate(userId);
    }

    @Override
    public void delete(final Long userId) {
        userWriteRepository.delete(userId);
    }
}
