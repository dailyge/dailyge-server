package project.dailyge.app.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.user.application.UserCacheReadService;
import project.dailyge.app.user.application.UserBlacklistWriteService;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserCacheReadService userCacheReadService;
    private final UserBlacklistWriteService userCacheWriteService;

    public void invalidateUser(final Long userId) {
        if (!userCacheReadService.existsById(userId)) {
            throw new IllegalArgumentException("올바른 사용자 ID를 입력해주세요.");
        }
        userCacheWriteService.deleteRefreshToken(userId);
        userCacheWriteService.saveBlacklistById(userId);
    }
}
