package project.dailyge.app.core.user.application;

import project.dailyge.core.cache.user.UserCache;

public interface UserCacheReadUseCase {
    UserCache findById(Long userId);
}
