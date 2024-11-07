package project.dailyge.core.cache.user;

public interface UserCacheWriteService {
    void save(UserCache userCache);

    void delete(Long userId);
}
