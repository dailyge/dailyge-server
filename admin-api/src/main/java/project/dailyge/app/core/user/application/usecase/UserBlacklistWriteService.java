package project.dailyge.app.core.user.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;

@ApplicationLayer
@RequiredArgsConstructor
public class UserBlacklistWriteService implements project.dailyge.core.cache.user.UserBlacklistWriteService {

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
