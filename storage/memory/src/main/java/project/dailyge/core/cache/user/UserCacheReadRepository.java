package project.dailyge.core.cache.user;

public interface UserCacheReadRepository {
    UserCache findById(Long userId);
}
