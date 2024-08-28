package project.dailyge.app.user.facade;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.user.application.UserBlacklistCreateCommand;
import project.dailyge.app.user.application.UserCacheReadService;
import project.dailyge.app.user.application.UserBlacklistWriteService;
import project.dailyge.app.user.exception.UserTypeException;
import static project.dailyge.app.user.exception.UserCodeAndMessage.USER_NOT_FOUND;

@FacadeLayer
@RequiredArgsConstructor
public class UserFacade {

    private final UserCacheReadService userCacheReadService;
    private final UserBlacklistWriteService userBlacklistWriteService;

    public void invalidateUser(
        final Long userId,
        final UserBlacklistCreateCommand command
    ) {
        if (!userCacheReadService.existsById(userId)) {
            throw UserTypeException.from(USER_NOT_FOUND);
        }
        userBlacklistWriteService.deleteRefreshToken(userId);
        userBlacklistWriteService.save(command.accessToken());
    }
}
