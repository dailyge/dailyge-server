package project.dailyge.app.test.user.integrationtest;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.user.presentation.LoginApi;
import project.dailyge.app.core.user.presentation.response.OAuthLoginResponse;
import project.dailyge.entity.user.UserEvent;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@RecordApplicationEvents
@DisplayName("[IntegrationTest] 사용자 로그인 통합 테스트")
class LoginIntegrationTest extends DatabaseTestBase {

    private static final String AUTHENTICATION_CODE = "AuthenticationCode";

    @Autowired
    private LoginApi loginApi;

    @Autowired
    private ApplicationEvents applicationEvents;

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    @DisplayName("로그인 성공 시, 응답에 DailygeToken 가 비어있지 않는다.")
    void whenLoginSuccessThenDailygeTokenShouldBeNotNull() {
        final ApiResponse<OAuthLoginResponse> response = loginApi.login(AUTHENTICATION_CODE);

        assertNotNull(response.getBody().getData().getAccessToken());
    }

    // @Test
    @DisplayName("로그인 성공 시 사용자 정보가 저장되도록, 이벤트가 발행된다.")
    void whenLoginSuccessThenCacheSaveEventShouldBeHappen() {
        loginApi.login(AUTHENTICATION_CODE);

        final long count = applicationEvents.stream(UserEvent.class).count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("구글 인증 API 중 에러 응답을 반환하면, ExternalServerException이 발생한다.")
    void whenAuthorizationFailedThenExternalServerExceptionShouldNotBeHappen() {
        stubFor(post(urlEqualTo("/authorization"))
            .willReturn(aResponse()
                .withStatus(BAD_GATEWAY.value())
                .withStatusMessage(BAD_GATEWAY.getReasonPhrase())
                .withBody(BAD_GATEWAY.getReasonPhrase())));

        assertThatThrownBy(() -> loginApi.login(AUTHENTICATION_CODE))
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

        assertThatThrownBy(() -> loginApi.login(AUTHENTICATION_CODE))
            .isExactlyInstanceOf(ExternalServerException.class)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(BAD_GATEWAY.value() + " " + BAD_GATEWAY.getReasonPhrase() + ": \"" + BAD_GATEWAY.getReasonPhrase() + "\"");
    }
}
