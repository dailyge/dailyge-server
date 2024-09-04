package project.dailyge.core.cache.user;

public interface UserCacheReadUseCase {
    UserCache findById(Long userId);

    boolean existsById(Long userId);
}
