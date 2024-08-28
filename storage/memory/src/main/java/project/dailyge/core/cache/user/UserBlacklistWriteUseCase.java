package project.dailyge.core.cache.user;

public interface UserBlacklistWriteUseCase {

    void saveBlacklistById(Long userId);

    void deleteRefreshToken(Long userId);
}
