package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import project.dailyge.app.core.common.web.BlacklistInterceptor;
import project.dailyge.app.core.common.web.UserBlacklistReadService;
import project.dailyge.core.cache.user.UserBlacklistReadRepository;
import project.dailyge.core.cache.user.UserBlacklistReadUseCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("[UnitTest] BlacklistInterceptor 단위 테스트")
class BlacklistInterceptorUnitTest {

    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String STRANGE_TOKEN = "Strange-Token";

    private BlacklistInterceptor blacklistInterceptor;
    private UserBlacklistReadRepository userBlacklistReadRepository;
    private UserBlacklistReadUseCase userBlacklistReadUseCase;
    private HttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        userBlacklistReadRepository = mock(UserBlacklistReadRepository.class);
        userBlacklistReadUseCase = new UserBlacklistReadService(userBlacklistReadRepository);
        blacklistInterceptor = new BlacklistInterceptor(userBlacklistReadUseCase);
        request = mock(HttpServletRequest.class);
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("블랙리스트에 있을 경우, false를 반환한다.")
    void whenExistsInBlacklistThenResultShouldBeFalse() {
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(ACCESS_TOKEN, ACCESS_TOKEN);

        when(request.getCookies()).thenReturn(cookies);
        when(userBlacklistReadRepository.existsByAccessToken(ACCESS_TOKEN)).thenReturn(true);

        assertFalse(blacklistInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("블랙리스트에 있을 경우, 리프레시 토큰을 삭제한다.")
    void whenExistsInBlacklistThenRefreshTokenShouldBeDeleted() {
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(ACCESS_TOKEN, ACCESS_TOKEN);

        when(request.getCookies()).thenReturn(cookies);
        when(userBlacklistReadRepository.existsByAccessToken(ACCESS_TOKEN)).thenReturn(true);

        blacklistInterceptor.preHandle(request, response, null);
        final Cookie accessTokenCookie = response.getCookie("Access-Token");
        final Cookie refreshTokenCookie = response.getCookie("Refresh-Token");

        assertEquals(0, accessTokenCookie.getMaxAge());
        assertEquals(0, refreshTokenCookie.getMaxAge());
    }

    @Test
    @DisplayName("AccessToken 쿠키가 null일 경우, true를 반환한다.")
    void whenAccessTokenCookieEmptyThenResultShouldBeTrue() {
        assertTrue(blacklistInterceptor.preHandle(request, response, null));
    }

    @Test
    @DisplayName("블랙리스트에 없을 경우, 어떠한 경우에도 true를 반환한다.")
    void whenNonExistsInBlacklistThenResultShouldBeTrue() {
        final Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(ACCESS_TOKEN, STRANGE_TOKEN);
        when(request.getCookies()).thenReturn(cookies);
        when(userBlacklistReadRepository.existsByAccessToken(STRANGE_TOKEN)).thenReturn(false);

        assertTrue(blacklistInterceptor.preHandle(request, response, null));
    }
}
