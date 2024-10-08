package project.dailyge.app.core.user.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;

@ApplicationLayer
@RequiredArgsConstructor
public class UserCacheWriteService implements project.dailyge.core.cache.user.UserCacheWriteService {

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
