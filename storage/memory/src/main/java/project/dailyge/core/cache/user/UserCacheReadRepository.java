package project.dailyge.core.cache.user;

public interface UserCacheReadRepository {
    UserCache findById(Long userId);

    boolean existsById(Long userId);
}
