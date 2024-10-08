package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.user.application.command.UserBlacklistCreateCommand;
import project.dailyge.app.core.user.application.usecase.UserCacheReadUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.core.cache.user.UserBlacklistWriteService;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@FacadeLayer
@RequiredArgsConstructor
public class UserFacade {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final UserBlacklistWriteService userBlacklistWriteService;

    public void invalidateUser(
        final Long userId,
        final UserBlacklistCreateCommand command
    ) {
        if (!userCacheReadUseCase.existsById(userId)) {
            throw UserTypeException.from(USER_NOT_FOUND);
        }
        userBlacklistWriteService.deleteRefreshToken(userId);
        userBlacklistWriteService.save(command.accessToken());
    }
}
