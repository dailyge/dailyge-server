package project.dailyge.app.core.user.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;
import project.dailyge.core.cache.user.UserCacheWriteService;

@ApplicationLayer
public class UserCacheWriteUseCase implements UserCacheWriteService {

    private final UserCacheWriteRepository userWriteRepository;

    public UserCacheWriteUseCase(final UserCacheWriteRepository userWriteRepository) {
        this.userWriteRepository = userWriteRepository;
    }

    @Override
    public void save(final UserCache userCache) {
        userWriteRepository.save(userCache);
    }

    @Override
    public void delete(final Long userId) {
        userWriteRepository.delete(userId);
    }
}
