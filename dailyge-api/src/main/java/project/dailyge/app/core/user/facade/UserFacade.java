package project.dailyge.app.core.user.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.dto.external.response.GoogleUserInfoResponse;
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

    public DailygeToken oAuthLogin(final String code) throws CommonException {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        GoogleUserInfoResponse response = googleOAuthClient.getAccessToken(clientRegistration, code);

        UserJpaEntity user = userWriteUseCase.upsert(new UserJpaEntity(response.getName(), response.getEmail(), response.getPicture()));
        DailygeToken token = createToken(user);
        saveToken(user, token.refreshToken());
        return token;
    }

    private DailygeToken createToken(UserJpaEntity user) {
        DailygeToken dailygeToken = tokenProvider.createToken(user);
        return dailygeToken;
    }

    private void saveToken(UserJpaEntity user, String refreshToken) {
        redisService.saveRefreshToken(user.getId(), refreshToken);
    }
}
