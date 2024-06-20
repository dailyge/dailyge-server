package project.dailyge.app.test.user.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.external.oauth.GoogleUserInfoClient;
import project.dailyge.app.core.user.presentation.response.external.GoogleAuthorizationResponse;
import project.dailyge.app.core.user.presentation.response.external.GoogleUserInfoResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OAuthUserInfoIntegrationTest extends DatabaseTestBase {

    @Value("${oauth.google.authorization}")
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final long EXPIRES_IN = 3600L;
    private static final String SCOPE = "email, openid";
    public static final String BEARER = "Bearer";

    @Autowired
    private GoogleUserInfoClient googleUserInfoClient;

    @Test
    @DisplayName("인증코드를 넘기면, 정상적으로 AccessToken 을 발급 받는다.")
    void whenSendAuthenticationCodeThenAccessTokenShouldBeReceive() {
        final GoogleAuthorizationResponse response = new GoogleAuthorizationResponse(ACCESS_TOKEN, EXPIRES_IN, SCOPE, BEARER);
        final GoogleUserInfoResponse userInfo = googleUserInfoClient.getUserInfo(response);

        assertAll(
            () -> assertEquals("1", userInfo.getId()),
            () -> assertEquals("test", userInfo.getName()),
            () -> assertEquals("test@gmail.com", userInfo.getEmail()),
            () -> assertEquals("test.url", userInfo.getPicture())
        );
    }
}
