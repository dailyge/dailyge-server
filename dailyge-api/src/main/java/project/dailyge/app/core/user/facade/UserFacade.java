package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEvent;

@FacadeLayer
@RequiredArgsConstructor
public class UserFacade {

    private final GoogleOAuthManager googleOAuthManager;
    private final UserReadUseCase userReadUseCase;
    private final UserWriteUseCase userWriteUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final ApplicationEventPublisher eventPublisher;
    private final UserCacheWriteUseCase userCacheWriteUseCase;

    public DailygeToken login(final String code) {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Long userId = getUserId(response);
        final UserCache userCache = new UserCache(
            userId, response.getName(), response.getEmail(), response.getPicture(), NORMAL.name()
        );
        userCacheWriteUseCase.save(userCache);

        final UserEvent userEvent = createEvent(userId, createTimeBasedUUID(), CREATE);
        eventPublisher.publishEvent(userEvent);

        final DailygeToken token = tokenProvider.createToken(userId, response.getEmail());
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long getUserId(final GoogleUserInfoResponse response) {
        final Long findUserId = userReadUseCase.findUserIdByEmail(response.getEmail());
        if (findUserId != null) {
            return findUserId;
        }
        final UserCache userCache = initUserCache(response);
        return userCache.getId();
    }

    private UserCache initUserCache(final GoogleUserInfoResponse response) {
        final UserCache userCache = new UserCache(
            userWriteUseCase.getSequence(),
            response.getName(),
            response.getEmail(),
            response.getPicture(),
            NORMAL.name()
        );
        userCacheWriteUseCase.save(userCache);
        return userCache;
    }

    public void logout(final Long userId) {
        tokenManager.deleteRefreshToken(userId);
    }

    public void delete(final Long userId) {
        userWriteUseCase.delete(userId);
        userCacheWriteUseCase.delete(userId);
        logout(userId);
    }
}
