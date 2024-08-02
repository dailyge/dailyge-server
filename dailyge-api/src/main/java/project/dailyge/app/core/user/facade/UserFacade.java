package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.user.UserCacheEvent;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final GoogleOAuthManager googleOAuthManager;
    private final UserWriteUseCase userWriteUseCase;
    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final ApplicationEventPublisher publisher;

    public DailygeToken login(final String code) throws CommonException {
        final GoogleUserInfoResponse response = googleOAuthManager.getUserInfo(code);
        final Optional<UserJpaEntity> findUserByEmail = userReadUseCase.findActiveUserByEmail(response.getEmail());
        final UserCache userCache = publishUserSave(findUserByEmail, response);

        final DailygeToken token = tokenProvider.createToken(userCache.getId(), response.getEmail());
        tokenManager.saveRefreshToken(userCache.getId(), token.refreshToken());
        return token;
    }

    private UserCache publishUserSave(
        final Optional<UserJpaEntity> findUserByEmail,
        final GoogleUserInfoResponse response
    ) {
        final UserCache userCache;
        if (findUserByEmail.isEmpty()) {
            final Long newUserSequence = userWriteUseCase.getSequence();
            userCache = new UserCache(newUserSequence, response.getName(), response.getEmail(), response.getPicture());
            // TODO - 여기에 추후 메시지큐 전송 로직 추가 예정.
        } else {
            final UserJpaEntity user = findUserByEmail.get();
            userCache = new UserCache(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImageUrl());
        }
        publisher.publishEvent(new UserCacheEvent(userCache));
        return userCache;
    }

    public void logout(final Long userId) {
        if (userId != null) {
            tokenManager.deleteRefreshToken(userId);
        }
    }
}
