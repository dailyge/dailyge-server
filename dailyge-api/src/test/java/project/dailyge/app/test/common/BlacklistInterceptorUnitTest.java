package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import project.dailyge.app.common.auth.JwtProperties;
import project.dailyge.app.common.auth.SecretKeyManager;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.web.BlacklistInterceptor;
import project.dailyge.app.core.common.web.UserBlacklistService;
import project.dailyge.core.cache.user.UserBlacklistReadRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@DisplayName("[UnitTest] BlacklistInterceptor 단위 테스트")
class BlacklistInterceptorUnitTest {

    private static final String SALT = "salt";
    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String STRANGE_TOKEN = "strangeToken";

    private BlacklistInterceptor blacklistInterceptor;
    private UserBlacklistReadRepository userBlacklistReadRepository;
    private UserBlacklistService userBlacklistService;
    private SecretKeyManager secretKeyManager;
    private TokenProvider tokenProvider;
    private HttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        final JwtProperties jwtProperties = new JwtProperties("secretKey", "payloadSecretKey", "salt", 0, 2);
        secretKeyManager = new SecretKeyManager(jwtProperties);
        tokenProvider = new TokenProvider(jwtProperties, secretKeyManager);
        userBlacklistReadRepository = mock(UserBlacklistReadRepository.class);
        userBlacklistService = new UserBlacklistService(userBlacklistReadRepository);
        blacklistInterceptor = new BlacklistInterceptor(userBlacklistService, tokenProvider);
        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("블랙리스트에 있을 경우, false를 반환한다.")
    void whenExistsInBlacklistThenResultShouldBeFalse() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(BEARER + ACCESS_TOKEN);
        when(userBlacklistReadRepository.existsByAccessToken(ACCESS_TOKEN)).thenReturn(true);

        assertFalse(blacklistInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("블랙리스트에 있을 경우, 리프레시 토큰을 삭제한다.")
    void whenExistsInBlacklistThenRefreshTokenShouldBeDeleted() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(BEARER + ACCESS_TOKEN);
        when(userBlacklistReadRepository.existsByAccessToken(ACCESS_TOKEN)).thenReturn(true);

        blacklistInterceptor.preHandle(request, response, null);
        final Cookie cookie = response.getCookie("Refresh-Token");

        assertEquals(0, cookie.getMaxAge());
    }

    @Test
    @DisplayName("Authorization 헤더가 null일 경우, true를 반환한다.")
    void whenAuthorizationEmptyThenResultShouldBeTrue() {
        assertTrue(blacklistInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("비 정상적인 토큰인 경우, true를 반환한다.")
    void whenAbnormalTokenThenResultShouldBeTrue() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(STRANGE_TOKEN);

        assertTrue(blacklistInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("블랙리스트에 없을 경우, 어떠한 경우에도 true를 반환한다.")
    void whenNonExistsInBlacklistThenResultShouldBeTrue() {
        when(request.getHeader(AUTHORIZATION)).thenReturn(BEARER + STRANGE_TOKEN);
        when(userBlacklistReadRepository.existsByAccessToken(STRANGE_TOKEN)).thenReturn(false);

        assertTrue(blacklistInterceptor.preHandle(request, response, null));
    }
}
