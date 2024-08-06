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
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.user.UserEvent;
import static project.dailyge.entity.user.UserEvent.createEvent;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Optional;

@Facade
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
        final Long userId = publishEvent(findUserByEmail, response);

        final DailygeToken token = tokenProvider.createToken(userId, response.getEmail());
        tokenManager.saveRefreshToken(userId, token.refreshToken());
        return token;
    }

    private Long publishEvent(
        final Optional<UserJpaEntity> findUserByEmail,
        final GoogleUserInfoResponse response
    ) {
        final Long userId = findUserByEmail.isEmpty() ? userWriteUseCase.getSequence() : findUserByEmail.get().getId();
        final UserEvent event = createEvent(
            userId,
            createTimeBasedUUID(),
            CREATE,
            response.getName(),
            response.getEmail(),
            response.getPicture()
        );
        publisher.publishEvent(event);
        return userId;
    }

    public void logout(final Long userId) {
        if (userId != null) {
            tokenManager.deleteRefreshToken(userId);
        }
    }
}
