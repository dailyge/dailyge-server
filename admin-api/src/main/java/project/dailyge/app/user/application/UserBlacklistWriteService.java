package project.dailyge.app.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.core.cache.user.UserBlacklistWriteUseCase;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;

@Service
@RequiredArgsConstructor
public class UserBlacklistWriteService implements UserBlacklistWriteUseCase {

    private final UserBlacklistWriteRepository userBlacklistWriteRepository;

    @Override
    public void saveBlacklistById(final Long userId) {
        userBlacklistWriteRepository.saveBlacklistById(userId);
    }

    @Override
    public void deleteRefreshToken(final Long userId) {
        userBlacklistWriteRepository.deleteRefreshToken(userId);
    }
}
