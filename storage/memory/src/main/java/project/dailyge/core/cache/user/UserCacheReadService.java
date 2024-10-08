package project.dailyge.core.cache.user;

public interface UserCacheReadService {
    UserCache findById(Long userId);

    boolean existsById(Long userId);
}
