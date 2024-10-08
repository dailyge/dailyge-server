package project.dailyge.core.cache.user;

public interface UserCacheWriteService {
    void save(UserCache userCache);

    void refreshExpirationDate(Long userId);

    void delete(Long userId);
}
