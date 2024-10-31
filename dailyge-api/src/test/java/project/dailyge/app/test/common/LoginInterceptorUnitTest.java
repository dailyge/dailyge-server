package project.dailyge.app.test.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockHttpServletResponse;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;
import project.dailyge.app.common.auth.SecretKeyManager;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.auth.DailygeToken;
import project.dailyge.app.core.common.auth.JwtProperties;
import project.dailyge.app.core.common.auth.LoginInterceptor;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadService;
import project.dailyge.entity.user.UserJpaEntity;

import java.io.UnsupportedEncodingException;

@DisplayName("[UnitTest] LoginInterceptor 단위 테스트")
class LoginInterceptorUnitTest {

    private static final String SECRET_KEY = "secretKey";
    private static final String PAYLOAD_SECRET_KEY = "payloadSecretKey";
    private static final String SALT = "salt";
    private static JwtProperties expiredJwtProperties;

    private LoginInterceptor loginInterceptor;
    private TokenProvider tokenProvider;
    private TokenManager tokenManager;
    private UserCacheReadService userCacheReadService;
    private HttpServletRequest request;
    private MockHttpServletResponse response;
    private SecretKeyManager secretKeyManager;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 1, 2);
        expiredJwtProperties = new JwtProperties(SECRET_KEY, PAYLOAD_SECRET_KEY, SALT, 0, 0);
        secretKeyManager = new SecretKeyManager(jwtProperties);
        tokenProvider = new TokenProvider(jwtProperties, secretKeyManager);
        userCacheReadService = mock(UserCacheReadService.class);
        tokenManager = mock(TokenManager.class);
        final ObjectMapper objectMapper = new ObjectMapper();
        loginInterceptor = new LoginInterceptor(userCacheReadService, tokenProvider, tokenManager, objectMapper);
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
        final DailygeToken token = tokenProvider.createToken(user.getId());

        final Cookie[] cookies = new Cookie[2];
        cookies[0] = new Cookie("Logged-In", "yes");
        cookies[1] = new Cookie("Access-Token", token.accessToken());
        when(request.getCookies()).thenReturn(cookies);
        when(userCacheReadService.existsById(user.getId())).thenReturn(true);

        assertFalse(loginInterceptor.preHandle(request, response, null));
        assertEquals(BAD_REQUEST.code(), response.getStatus());
    }

    @Test
    @DisplayName("토큰 기간이 만료된 사용자는 재갱신하고, false 를 반환한다.")
    void whenSuccessRefreshForExpiredUserThenResultShouldBeFalse() throws UnsupportedEncodingException, JSONException {
        final UserJpaEntity user = createUser(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId());
        final TokenProvider expiredTokenProvider = new TokenProvider(expiredJwtProperties, secretKeyManager);
        final DailygeToken expiredToken = expiredTokenProvider.createToken(user.getId());
        final Cookie[] cookies = new Cookie[3];
        cookies[0] = new Cookie("Logged-In", "yes");
        cookies[1] = new Cookie("Refresh-Token", token.refreshToken());
        cookies[2] = new Cookie("Access-Token", expiredToken.accessToken());

        when(request.getCookies()).thenReturn(cookies);
        when(userCacheReadService.existsById(user.getId()))
            .thenReturn(true);
        when(userCacheReadService.findById(user.getId()))
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
        final Cookie accessTokencookie = response.getCookie("Access-Token");

        assertFalse(result);
        assertNotEquals(cookies[1].getValue(), accessTokencookie.getValue());
    }

    @Test
    @DisplayName("토큰 기간이 만료된 사용자는 재갱신 실패 시, true 를 반환한다.")
    void whenFailedRefreshForExpiredUserThenResultShouldBeTrue() {
        final TokenProvider expiredTokenProvider = new TokenProvider(expiredJwtProperties, secretKeyManager);
        final UserJpaEntity user = createUser(1L);
        final DailygeToken expiredToken = expiredTokenProvider.createToken(user.getId());
        final Cookie[] cookies = new Cookie[3];
        cookies[0] = new Cookie("Logged-In", "yes");
        cookies[1] = new Cookie("Refresh-Token", expiredToken.refreshToken());
        cookies[2] = new Cookie("Access-Token", expiredToken.accessToken());
        when(request.getCookies()).thenReturn(cookies);

        assertTrue(loginInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("LoggedIn 쿠키가 없다면, true 를 반환한다.")
    void whenLoggedInIsEmptyThenResultShouldBeTrue() {
        final UserJpaEntity user = createUser(1L);
        final DailygeToken token = tokenProvider.createToken(user.getId());

        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("Access-Token", token.accessToken());
        when(request.getCookies()).thenReturn(cookies);
        when(userCacheReadService.existsById(user.getId())).thenReturn(true);

        assertTrue(loginInterceptor.preHandle(request, response, null));
    }
}
