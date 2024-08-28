package project.dailyge.core.cache.user;

public interface UserBlacklistWriteRepository {

    void saveBlacklistById(Long userId);

    void deleteRefreshToken(Long userId);
}
