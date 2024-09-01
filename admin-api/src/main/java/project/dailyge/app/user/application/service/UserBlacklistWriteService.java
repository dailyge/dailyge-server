package project.dailyge.app.user.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserBlacklistWriteUseCase;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;

@ApplicationLayer
@RequiredArgsConstructor
public class UserBlacklistWriteService implements UserBlacklistWriteUseCase {

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
