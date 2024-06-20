package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.external.oauth.GoogleAccessClient;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[IntegrationTest] OAuth2 로그인 검증 통합 테스트")
public class OAuthAccessIntegrationTest extends DatabaseTestBase {

    private static final String AUTHENTICATION_CODE = "AuthenticationCode";
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final long EXPIRES_IN = 3600L;
    private static final String SCOPE = "email, openid";
    public static final String BEARER = "Bearer";

    @Autowired
    private GoogleAccessClient googleAccessClient;

    @Test
    @DisplayName("인증코드를 넘기면, 정상적으로 AccessToken 을 발급 받는다.")
    void whenSendAuthenticationCodeThenAccessTokenShouldBeReceive() {
        GoogleAuthorizationResponse response = googleAccessClient.userAccess(AUTHENTICATION_CODE);

        assertAll(
            () -> assertEquals(ACCESS_TOKEN, response.getAccessToken()),
            () -> assertEquals(EXPIRES_IN, response.getExpiresIn()),
            () -> assertEquals(SCOPE, response.getScope()),
            () -> assertEquals(BEARER, response.getTokenType())
        );
    }
}
