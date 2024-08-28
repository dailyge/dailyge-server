package project.dailyge.app.core.common.web;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.core.cache.user.UserBlacklistReadRepository;
import project.dailyge.core.cache.user.UserBlacklistReadUseCase;

@Service
@RequiredArgsConstructor
public class UserBlacklistReadService implements UserBlacklistReadUseCase {

    private final UserBlacklistReadRepository userBlacklistReadRepository;

    @Override
    public Date getBlacklistById(final Long userId) {
        return userBlacklistReadRepository.getBlacklistById(userId);
    }
}
