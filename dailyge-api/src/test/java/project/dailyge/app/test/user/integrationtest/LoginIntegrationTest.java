package project.dailyge.app.test.user.integrationtest;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.presentation.OAuthApi;
import project.dailyge.app.core.user.presentation.response.OAuthLoginResponse;
import project.dailyge.entity.user.UserJpaEntity;

import java.io.IOException;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static project.dailyge.app.fixture.user.OAuthFixture.getOAuthAccessResponseFixture;

@DisplayName("[IntegrationTest] 사용자 로그인 통합 테스트")
class LoginIntegrationTest extends DatabaseTestBase {

    private static final String AUTHENTICATION_CODE = "AuthenticationCode";

    @Autowired
    private OAuthApi oAuthApi;

    @Autowired
    private UserReadUseCase userReadUseCase;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    @DisplayName("로그인에 성공하면, DailygeToken을 생성한다.")
    void whenLoginSuccessThenDailygeTokenIsGenerated() throws IOException {
        ApiResponse<OAuthLoginResponse> login = oAuthApi.login(AUTHENTICATION_CODE);
        Long userId = tokenProvider.getUserId(Objects.requireNonNull(login.getBody()).getData().getAccessToken());
        UserJpaEntity findUser = userReadUseCase.findActiveUserById(userId);

        Assertions.assertEquals(getOAuthAccessResponseFixture().get("email"), findUser.getEmail());
    }

    @Test
    @DisplayName("구글 인증 API 중 에러 응답을 반환하면, ExternalServerException이 발생한다.")
    void whenAuthorizationFailedThenExternalServerExceptionShouldNotBeHappen() {
        stubFor(post(urlEqualTo("/authorization"))
            .willReturn(aResponse()
                .withStatus(BAD_GATEWAY.value())
                .withStatusMessage(BAD_GATEWAY.getReasonPhrase())
                .withBody(BAD_GATEWAY.getReasonPhrase())));

        assertThatThrownBy(() -> oAuthApi.login(AUTHENTICATION_CODE))
            .isExactlyInstanceOf(ExternalServerException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(BAD_GATEWAY.value() + " " + BAD_GATEWAY.getReasonPhrase() + ": \"" + BAD_GATEWAY.getReasonPhrase() + "\"");
    }

    @Test
    @DisplayName("구글 사용자 정보 가져오는 중 에러 응답을 반환하면, ExternalServerException이 발생한다.")
    void whenUserInfoGetFailedThenExternalServerExceptionShouldNotBeHappen() {
        stubFor(get(urlEqualTo("/userinfo"))
            .willReturn(aResponse()
                .withStatus(BAD_GATEWAY.value())
                .withStatusMessage(BAD_GATEWAY.getReasonPhrase())
                .withBody(BAD_GATEWAY.getReasonPhrase())));

        assertThatThrownBy(() -> oAuthApi.login(AUTHENTICATION_CODE))
            .isExactlyInstanceOf(ExternalServerException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(BAD_GATEWAY.value() + " " + BAD_GATEWAY.getReasonPhrase() + ": \"" + BAD_GATEWAY.getReasonPhrase() + "\"");
    }
}
