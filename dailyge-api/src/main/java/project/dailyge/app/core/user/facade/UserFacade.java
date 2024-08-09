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
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import static project.dailyge.entity.user.Role.NORMAL;
import static project.dailyge.entity.user.UserEvent.createEvent;

@Facade
@RequiredArgsConstructor
public class UserFacade {

    private final GoogleOAuthManager googleOAuthManager;
    private final UserWriteUseCase userWriteUseCase;
    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final ApplicationEventPublisher publisher;
    private final UserCacheWriteUseCase userCacheWriteUseCase;

    public DailygeToken login(final String code) throws CommonException {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Optional<UserJpaEntity> findUserByEmail = userReadUseCase.findActiveUserByEmail(response.getEmail());
        final Long userId = saveCache(findUserByEmail, response);
        final UserEvent userEvent = createEvent(userId, createTimeBasedUUID(), CREATE);
        publisher.publishEvent(userEvent);

        final DailygeToken token = tokenProvider.createToken(userId, response.getEmail());
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long saveCache(
        final Optional<UserJpaEntity> findUserByEmail,
        final GoogleUserInfoResponse response
    ) {
        final UserCache userCache;
        if (findUserByEmail.isPresent()) {
            final UserJpaEntity user = findUserByEmail.get();
            userCache = new UserCache(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getRoleToString()
            );
        } else {
            userCache = new UserCache(
                userWriteUseCase.getSequence(),
                response.getName(),
                response.getEmail(),
                response.getPicture(),
                NORMAL.name()
            );
        }
        userCacheWriteUseCase.save(userCache);
        return userCache.getId();
    }

    public void logout(final Long userId) {
        if (userId != null) {
            tokenManager.deleteRefreshToken(userId);
        }
    }
}
