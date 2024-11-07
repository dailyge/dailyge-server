package project.dailyge.core.cache.user;

public interface UserCacheWriteRepository {
    void save(UserCache userCache);

    void delete(Long userId);
}
