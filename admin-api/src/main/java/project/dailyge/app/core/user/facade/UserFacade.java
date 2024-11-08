package project.dailyge.app.core.user.facade;

import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.user.application.command.UserBlacklistCreateCommand;
import project.dailyge.app.core.user.application.usecase.UserCacheReadUseCase;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_FOUND;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.core.cache.user.UserBlacklistWriteService;

@FacadeLayer
public class UserFacade {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final UserBlacklistWriteService userBlacklistWriteService;

    public UserFacade(
        final UserCacheReadUseCase userCacheReadUseCase,
        final UserBlacklistWriteService userBlacklistWriteService
    ) {
        this.userCacheReadUseCase = userCacheReadUseCase;
        this.userBlacklistWriteService = userBlacklistWriteService;
    }

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
