package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;
import project.dailyge.app.core.user.external.oauth.GoogleOAuthClient;
import project.dailyge.app.core.user.external.redis.service.RedisService;
import project.dailyge.domain.user.UserJpaEntity;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final GoogleOAuthClient googleOAuthClient;
    private final UserWriteUseCase userWriteUseCase;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    public DailygeToken login(final String code) throws CommonException {
        final ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        final GoogleUserInfoResponse response = googleOAuthClient.getAccessToken(clientRegistration, code);

        final UserJpaEntity user = userWriteUseCase.upsert(new UserJpaEntity(response.getName(), response.getEmail(), response.getPicture()));
        final DailygeToken token = createToken(user);
        saveRefreshToken(user, token.refreshToken());
        return token;
    }

    private DailygeToken createToken(final UserJpaEntity user) {
        final DailygeToken dailygeToken = tokenProvider.createToken(user);
        return dailygeToken;
    }

    private void saveRefreshToken(final UserJpaEntity user, final String refreshToken) {
        redisService.saveRefreshToken(user.getId(), refreshToken);
    }
}
