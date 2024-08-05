package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthManager;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.core.user.external.response.GoogleUserInfoResponse;
import project.dailyge.entity.common.EventType;
import project.dailyge.entity.user.UserEvent;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;

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
        final Long userId = eventPublish(findUserByEmail, response);

        final DailygeToken token = tokenProvider.createToken(userId, response.getEmail());
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long eventPublish(
        final Optional<UserJpaEntity> findUserByEmail,
        final GoogleUserInfoResponse response
    ) {
        final Long userId = findUserByEmail.isEmpty() ? userWriteUseCase.getSequence() : findUserByEmail.get().getId();
        final UserEvent event = UserEvent.createEvent(
            userId,
            createTimeBasedUUID(),
            EventType.CREATE,
            response.getName(),
            response.getEmail(),
            response.getPicture()
        );
        publisher.publishEvent(event);
        return userId;
    }

    public void logout(final Long userId) {
        tokenManager.deleteRefreshToken(userId);
    }
}
