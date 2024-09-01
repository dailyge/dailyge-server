package project.dailyge.core.cache.user;

public interface UserBlacklistWriteRepository {
    void save(String accessToken);

    void deleteRefreshToken(Long userId);
}
