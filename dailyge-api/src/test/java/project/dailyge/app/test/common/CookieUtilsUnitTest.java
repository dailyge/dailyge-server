package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.utils.CookieUtils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.common.utils.CookieUtils.createCookie;
import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;

@DisplayName("[UnitTest] CookieUtils 단위 테스트")
class CookieUtilsUnitTest {

    private static final String REFRESH_TOKEN = "dg_res";
    private static final String COOKIE_PATH = "/app";
    private static final long MAX_AGE = 86_400L;
    private static final String COOKIE_VALUE = "testValue";

    @Test
    @DisplayName("만료할 쿠키를 생성하면, 정상적으로 생성된다.")
    void whenCreateCookieToExpireThenMaxAgeShouldBeZero() {
        final Cookie cookie = CookieUtils.clearCookie(REFRESH_TOKEN);
        assertAll(
            () -> assertEquals(REFRESH_TOKEN, cookie.getName()),
            () -> assertEquals(0, cookie.getMaxAge()),
            () -> assertEquals(".dailyge.com", cookie.getDomain()),
            () -> assertEquals("/", cookie.getPath()),
            () -> assertFalse(cookie.getSecure()),
            () -> assertFalse(cookie.isHttpOnly()),
            () -> assertNull(cookie.getValue())
        );
    }

    @Test
    @DisplayName("만료할 Response 쿠키를 생성하면, 정상적으로 생성된다.")
    void whenClearResponseCookieThenCorrectlyGenerated() {
        final String cookieString = CookieUtils.clearResponseCookie(REFRESH_TOKEN, true);
        assertAll(
            () -> assertTrue(cookieString.contains("dg_res")),
            () -> assertTrue(cookieString.contains("Max-Age=0")),
            () -> assertTrue(cookieString.contains("Domain=.dailyge.com")),
            () -> assertTrue(cookieString.contains("Path=/")),
            () -> assertFalse(cookieString.contains("Secure")),
            () -> assertFalse(cookieString.contains("HttpOnly"))
        );
    }

    @Test
    @DisplayName("로컬 환경에서 생성된 쿠키는 도메인이 비어 있다.")
    void whenCreateResponseCookieInLocalEnvThenDomainIsEmpty() {
        final String cookieString = createResponseCookie(
            REFRESH_TOKEN, COOKIE_VALUE, COOKIE_PATH, MAX_AGE, true, "local"
        );
        assertAll(
            () -> assertTrue(cookieString.contains("dg_res=testValue")),
            () -> assertTrue(cookieString.contains("Max-Age=86400")),
            () -> assertTrue(cookieString.contains("Path=" + COOKIE_PATH)),
            () -> assertFalse(cookieString.contains("Domain="))
        );
    }

    @Test
    @DisplayName("배포 환경에서 생성된 Response 쿠키는 Secure와 HttpOnly가 설정된다.")
    void whenCreateResponseCookieInProdEnvThenHttpOnlyAndSecureAreSet() {
        final String cookieString = createResponseCookie(
            REFRESH_TOKEN, COOKIE_VALUE, COOKIE_PATH, MAX_AGE, true, "prod"
        );
        assertAll(
            () -> assertTrue(cookieString.contains("dg_res=testValue")),
            () -> assertTrue(cookieString.contains("Max-Age=86400")),
            () -> assertTrue(cookieString.contains("Domain=.dailyge.com")),
            () -> assertTrue(cookieString.contains("Path=" + COOKIE_PATH)),
            () -> assertTrue(cookieString.contains("Secure")),
            () -> assertTrue(cookieString.contains("HttpOnly"))
        );
    }

    @Test
    @DisplayName("파라미터로 값을 넣어 쿠키를 생성할 수 있다.")
    void whenCreateCookieThenCorrectlyGenerated() {
        final Cookie cookie = createCookie(REFRESH_TOKEN, COOKIE_VALUE, COOKIE_PATH, (int) MAX_AGE);
        assertAll(
            () -> assertEquals(REFRESH_TOKEN, cookie.getName()),
            () -> assertEquals(COOKIE_VALUE, cookie.getValue()),
            () -> assertEquals((int) MAX_AGE, cookie.getMaxAge()),
            () -> assertEquals(".dailyge.com", cookie.getDomain()),
            () -> assertEquals(COOKIE_PATH, cookie.getPath()),
            () -> assertFalse(cookie.getSecure()),
            () -> assertFalse(cookie.isHttpOnly())
        );
    }
}
