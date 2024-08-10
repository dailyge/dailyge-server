package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import project.dailyge.app.common.annotation.Facade;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.user.Role.NORMAL;
import static project.dailyge.entity.user.UserEvent.createEvent;

@Facade
@RequiredArgsConstructor
public class UserFacade {

    private final GoogleOAuthManager googleOAuthManager;
    private final UserReadUseCase userReadUseCase;
    private final UserWriteUseCase userWriteUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final ApplicationEventPublisher eventPublisher;
    private final UserCacheReadUseCase userCacheReadUseCase;
    private final UserCacheWriteUseCase userCacheWriteUseCase;

    public DailygeToken login(final String code) throws CommonException {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Long userId = upsertUserCache(response);

        final DailygeToken token = tokenProvider.createToken(userId, response.getEmail());
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long upsertUserCache(final GoogleUserInfoResponse response) {
        final Long findUserId = userReadUseCase.findUserIdByEmail(response.getEmail());
        if (findUserId == null) {
            return createUserCache(response);
        } else {
            return refreshUserCache(findUserId);
        }
    }

    private Long createUserCache(final GoogleUserInfoResponse response) {
        final Long userId = saveCache(
            userWriteUseCase.getSequence(),
            response.getName(),
            response.getEmail(),
            response.getPicture()
        );
        final UserEvent userEvent = createEvent(userId, createTimeBasedUUID(), CREATE);
        eventPublisher.publishEvent(userEvent);
        return userId;
    }

    private Long refreshUserCache(final Long userId) {
        if (userCacheReadUseCase.existsById(userId)) {
            userCacheWriteUseCase.refreshExpirationDate(userId);
        } else {
            final UserJpaEntity user = userReadUseCase.findActiveUserById(userId);
            saveCache(
                userId,
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl()
            );
        }
        return userId;
    }

    private Long saveCache(
        final Long userId,
        final String name,
        final String email,
        final String profileImageUrl
    ) {
        final UserCache userCache = new UserCache(
            userId,
            name,
            email,
            profileImageUrl,
            NORMAL.name()
        );
        userCacheWriteUseCase.save(userCache);
        return userCache.getId();
    }

    public void logout(final Long userId) {
        if (userId != null) {
            tokenManager.deleteRefreshToken(userId);
        }
    }
}
