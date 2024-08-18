package project.dailyge.app.test.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.SecretKeyManager;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.web.LoginInterceptor;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.entity.user.UserJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;

@DisplayName("[UnitTest] LoginInterceptor 단위 테스트")
class LoginInterceptorUnitTest {

    private static final String SECRET_KEY = "secretKey";
    private static final String PAYLOAD_SECRET_KEY = "payloadSecretKey";
    private static final String SALT = "salt";
    private static JwtProperties expiredJwtProperties;

    private LoginInterceptor loginInterceptor;
    private TokenProvider tokenProvider;
    private TokenManager tokenManager;
    private UserCacheReadUseCase userCacheReadUseCase;
    private HttpServletRequest request;
    private MockHttpServletResponse response;
    private SecretKeyManager secretKeyManager;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 1, 2);
        expiredJwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 0, 0);
        secretKeyManager = new SecretKeyManager(jwtProperties);
        tokenProvider = new TokenProvider(jwtProperties, secretKeyManager);
        userCacheReadUseCase = mock(UserCacheReadUseCase.class);
        tokenManager = mock(TokenManager.class);
        final ObjectMapper objectMapper = new ObjectMapper();
        loginInterceptor = new LoginInterceptor(userCacheReadUseCase, tokenProvider, tokenManager, objectMapper);
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
        final UserJpaEntity user = createUser(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId(), user.getEmail());

        when(request.getHeader(AUTHORIZATION))
            .thenReturn(token.getAuthorizationToken());
        when(userCacheReadUseCase.existsById(user.getId()))
            .thenReturn(true);

        assertFalse(loginInterceptor.preHandle(request, response, null));
        assertEquals(UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @DisplayName("토큰 기간이 만료된 사용자는 재갱신하고, false 를 반환한다.")
    void whenSuccessRefreshForExpiredUserThenResultShouldBeFalse() throws UnsupportedEncodingException, JSONException {
        final UserJpaEntity user = createUser(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId(), user.getEmail());
        final TokenProvider expiredTokenProvider = new TokenProvider(expiredJwtProperties, secretKeyManager);
        final DailygeToken expiredToken = expiredTokenProvider.createToken(user.getId(), user.getEmail());
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Refresh-Token", token.refreshToken());

        when(request.getCookies())
            .thenReturn(cookies);
        when(request.getHeader(AUTHORIZATION))
            .thenReturn(expiredToken.getAuthorizationToken());
        when(userCacheReadUseCase.existsById(user.getId()))
            .thenReturn(true);
        when(userCacheReadUseCase.findById(user.getId()))
            .thenReturn(new UserCache(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getRoleAsString()
            ));
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
        final TokenProvider expiredTokenProvider = new TokenProvider(expiredJwtProperties, secretKeyManager);
        final UserJpaEntity user = createUser(1L);
        final DailygeToken expiredToken = expiredTokenProvider.createToken(user.getId(), user.getEmail());
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Refresh-Token", null);

        when(request.getCookies())
            .thenReturn(cookies);
        when(request.getHeader(AUTHORIZATION))
            .thenReturn(expiredToken.getAuthorizationToken());

        assertTrue(loginInterceptor.preHandle(request, response, null));
    }
}
