package project.dailyge.core.cache.user;

public interface UserBlacklistReadRepository {
    boolean existsByAccessToken(String accessToken);
}
