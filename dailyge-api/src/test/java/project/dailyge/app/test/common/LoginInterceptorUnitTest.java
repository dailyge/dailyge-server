package project.dailyge.app.test.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.web.LoginInterceptor;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.app.test.user.fixture.UserFixture;
import project.dailyge.entity.user.UserJpaEntity;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@DisplayName("[UnitTest] LoginInterceptor 단위 테스트")
class LoginInterceptorUnitTest {

    private static final String SECRET_KEY = "secretKey";
    private static final String PAYLOAD_SECRET_KEY = "payloadSecretKey";
    private static final String SALT = "salt";

    private LoginInterceptor loginInterceptor;
    private TokenProvider tokenProvider;
    private TokenManager tokenManager;
    private UserReadUseCase userReadUseCase;
    private HttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 1, 2);
        tokenProvider = new TokenProvider(jwtProperties);
        userReadUseCase = mock(UserReadUseCase.class);
        tokenManager = mock(TokenManager.class);
        final ObjectMapper objectMapper = new ObjectMapper();
        loginInterceptor = new LoginInterceptor(userReadUseCase, tokenProvider, tokenManager, objectMapper);
        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("비로그인 사용자가 로그인 호출 시, true 를 반환한다.")
    void whenNonLoginUserCallsToLoginThenResultShouldBeTrue() {
        assertTrue(loginInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("로그인 사용자가 로그인 호출 시, false 를 반환한다.")
    void whenLoginUserCallsToLoginThenResultShouldBeFalse() {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user);

        when(request.getHeader(AUTHORIZATION))
            .thenReturn(token.getAuthorizationToken());
        when(userReadUseCase.existsById(user.getId()))
            .thenReturn(true);

        assertFalse(loginInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("토큰 기간이 만료된 사용자는 재갱신하고, false 를 반환한다.")
    void whenSuccessRefreshForExpiredUserThenResultShouldBeFalse() throws UnsupportedEncodingException, JSONException {
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken token = tokenProvider.createToken(user);
        final JwtProperties expiredJwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 0, 0);
        final TokenProvider expiredTokenProvider = new TokenProvider(expiredJwtProperties);
        final DailygeToken expiredToken = expiredTokenProvider.createToken(user);
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Refresh-Token", token.refreshToken());

        when(request.getCookies())
            .thenReturn(cookies);
        when(request.getHeader(AUTHORIZATION))
            .thenReturn(expiredToken.getAuthorizationToken());
        when(userReadUseCase.existsById(user.getId()))
            .thenReturn(true);
        when(userReadUseCase.findActiveUserById(user.getId()))
            .thenReturn(user);
        when(tokenManager.getRefreshToken(user.getId()))
            .thenReturn(token.refreshToken());

        final boolean result = loginInterceptor.preHandle(request, response, null);
        final JSONObject jsonObject = new JSONObject(response.getContentAsString());
        final String newAccessToken = jsonObject.getString("Access-Token");

        assertFalse(result);
        assertNotEquals(expiredToken.accessToken(), newAccessToken);
    }

    @Test
    @DisplayName("토큰 기간이 만료된 사용자는 재갱신 실패 시, true 를 반환한다.")
    void whenFailedRefreshForExpiredUserThenResultShouldBeTrue() {
        final JwtProperties expiredJwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 0, 0);
        final TokenProvider expiredTokenProvider = new TokenProvider(expiredJwtProperties);
        final UserJpaEntity user = UserFixture.createUserJpaEntity(1L);
        final DailygeToken expiredToken = expiredTokenProvider.createToken(user);
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Refresh-Token", null);

        when(request.getCookies())
            .thenReturn(cookies);
        when(request.getHeader(AUTHORIZATION))
            .thenReturn(expiredToken.getAuthorizationToken());

        assertTrue(loginInterceptor.preHandle(request, response, null));
    }
}
