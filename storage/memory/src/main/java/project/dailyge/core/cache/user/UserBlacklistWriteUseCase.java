package project.dailyge.core.cache.user;

public interface UserBlacklistWriteUseCase {
    void save(String accessToken);

    void deleteRefreshToken(Long userId);
}
