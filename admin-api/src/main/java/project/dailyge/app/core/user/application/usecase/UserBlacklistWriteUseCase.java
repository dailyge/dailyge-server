package project.dailyge.app.core.user.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;
import project.dailyge.core.cache.user.UserBlacklistWriteService;

@ApplicationLayer
@RequiredArgsConstructor
public class UserBlacklistWriteUseCase implements UserBlacklistWriteService {

    private final UserBlacklistWriteRepository userBlacklistWriteRepository;

    @Override
    public void save(final String accessToken) {
        userBlacklistWriteRepository.save(accessToken);
    }

    @Override
    public void deleteRefreshToken(final Long userId) {
        userBlacklistWriteRepository.deleteRefreshToken(userId);
    }
}
