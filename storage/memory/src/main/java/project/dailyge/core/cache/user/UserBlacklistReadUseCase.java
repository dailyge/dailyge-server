package project.dailyge.core.cache.user;

public interface UserBlacklistReadUseCase {
    boolean existsByAccessToken(String accessToken);
}
