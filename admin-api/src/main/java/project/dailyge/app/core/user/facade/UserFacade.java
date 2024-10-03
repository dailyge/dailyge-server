package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.user.application.command.UserBlacklistCreateCommand;
import project.dailyge.app.core.user.application.service.UserCacheReadService;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.core.cache.user.UserBlacklistWriteUseCase;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@FacadeLayer
@RequiredArgsConstructor
public class UserFacade {

    private final UserCacheReadService userCacheReadService;
    private final UserBlacklistWriteUseCase userBlacklistWriteUseCase;

    public void invalidateUser(
        final Long userId,
        final UserBlacklistCreateCommand command
    ) {
        if (!userCacheReadService.existsById(userId)) {
            throw UserTypeException.from(USER_NOT_FOUND);
        }
        userBlacklistWriteUseCase.deleteRefreshToken(userId);
        userBlacklistWriteUseCase.save(command.accessToken());
    }
}
