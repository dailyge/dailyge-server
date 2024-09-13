package project.dailyge.app.core.common.web;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.core.cache.user.UserBlacklistReadRepository;
import project.dailyge.core.cache.user.UserBlacklistReadUseCase;

@RequiredArgsConstructor
@ApplicationLayer(value = "UserBlacklistReadService")
public class UserBlacklistReadService implements UserBlacklistReadUseCase {

    private final UserBlacklistReadRepository userBlacklistReadRepository;

    @Override
    public boolean existsByAccessToken(final String accessToken) {
        return userBlacklistReadRepository.existsByAccessToken(accessToken);
    }
}
