package project.dailyge.app.core.user.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;
import project.dailyge.core.cache.user.UserBlacklistWriteService;

@ApplicationLayer
public class UserBlacklistWriteUseCase implements UserBlacklistWriteService {

    private final UserBlacklistWriteRepository userBlacklistWriteRepository;

    public UserBlacklistWriteUseCase(final UserBlacklistWriteRepository userBlacklistWriteRepository) {
        this.userBlacklistWriteRepository = userBlacklistWriteRepository;
    }

    @Override
    public void save(final String accessToken) {
        userBlacklistWriteRepository.save(accessToken);
    }

    @Override
    public void deleteRefreshToken(final Long userId) {
        userBlacklistWriteRepository.deleteRefreshToken(userId);
    }
}
