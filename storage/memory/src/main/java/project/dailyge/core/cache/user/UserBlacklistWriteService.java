package project.dailyge.core.cache.user;

public interface UserBlacklistWriteService {
    void save(String accessToken);

    void deleteRefreshToken(Long userId);
}
