package project.dailyge.app.test.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.common.auth.DailygeToken;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[UnitTest] DailygeToken 단위 테스트")
class DailygeTokenUnitTest {

    private static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
    private static final String REFRESH_TOKEN_VALUE = "refreshTokenValue";
    private static final int ACCESS_TOKEN_MAX_AGE = 1_800;
    private static final int REFRESH_TOKEN_MAX_AGE = 2_592_000;
    private static final String ENV_PROD = "prod";
    private static final String ENV_LOCAL = "local";

    @Test
    @DisplayName("Access Token 쿠키를 올바르게 생성할 수 있다.")
    void whenGetAccessTokenCookieThenCorrectlyGenerated() {
        final DailygeToken token = new DailygeToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE, ACCESS_TOKEN_MAX_AGE, REFRESH_TOKEN_MAX_AGE);
        final String accessTokenCookie = token.getAccessTokenCookie(ENV_PROD);
        assertAll(
            () -> assertTrue(accessTokenCookie.contains("Access-Token=accessTokenValue")),
            () -> assertTrue(accessTokenCookie.contains("Max-Age=1800")),
            () -> assertTrue(accessTokenCookie.contains("Path=/")),
            () -> assertTrue(accessTokenCookie.contains("Secure")),
            () -> assertTrue(accessTokenCookie.contains("HttpOnly")),
            () -> assertTrue(accessTokenCookie.contains("Domain=.dailyge.com"))
        );
    }

    @Test
    @DisplayName("Refresh Token 쿠키를 올바르게 생성할 수 있다.")
    void whenGetRefreshTokenCookieThenCorrectlyGenerated() {
        final DailygeToken token = new DailygeToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE, ACCESS_TOKEN_MAX_AGE, REFRESH_TOKEN_MAX_AGE);
        final String refreshTokenCookie = token.getRefreshTokenCookie(ENV_PROD);
        assertAll(
            () -> assertTrue(refreshTokenCookie.contains("Refresh-Token=refreshTokenValue")),
            () -> assertTrue(refreshTokenCookie.contains("Max-Age=2592000")),
            () -> assertTrue(refreshTokenCookie.contains("Path=/")),
            () -> assertTrue(refreshTokenCookie.contains("Secure")),
            () -> assertTrue(refreshTokenCookie.contains("HttpOnly")),
            () -> assertTrue(refreshTokenCookie.contains("Domain=.dailyge.com"))
        );
    }

    @Test
    @DisplayName("로컬 환경에서 Domain 없이 Access Token 쿠키를 생성할 수 있다.")
    void whenGetAccessTokenCookieInLocalThenDomainIsEmpty() {
        final DailygeToken token = new DailygeToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE, ACCESS_TOKEN_MAX_AGE, REFRESH_TOKEN_MAX_AGE);
        final String accessTokenCookie = token.getAccessTokenCookie(ENV_LOCAL);
        assertAll(
            () -> assertTrue(accessTokenCookie.contains("Access-Token=accessTokenValue")),
            () -> assertTrue(accessTokenCookie.contains("Max-Age=1800")),
            () -> assertTrue(accessTokenCookie.contains("Path=/")),
            () -> assertFalse(accessTokenCookie.contains("Domain=")),
            () -> assertFalse(accessTokenCookie.contains("Secure")),
            () -> assertTrue(accessTokenCookie.contains("HttpOnly"))
        );
    }

    @Test
    @DisplayName("로컬 환경에서 Domain 없이 Refresh Token 쿠키를 생성할 수 있다.")
    void whenGetRefreshTokenCookieInLocalThenDomainIsEmpty() {
        final DailygeToken token = new DailygeToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE, ACCESS_TOKEN_MAX_AGE, REFRESH_TOKEN_MAX_AGE);
        final String refreshTokenCookie = token.getRefreshTokenCookie(ENV_LOCAL);
        assertAll(
            () -> assertTrue(refreshTokenCookie.contains("Refresh-Token=refreshTokenValue")),
            () -> assertTrue(refreshTokenCookie.contains("Max-Age=2592000")),
            () -> assertTrue(refreshTokenCookie.contains("Path=/")),
            () -> assertFalse(refreshTokenCookie.contains("Domain=")),
            () -> assertFalse(refreshTokenCookie.contains("Secure")),
            () -> assertTrue(refreshTokenCookie.contains("HttpOnly"))
        );
    }

    @Test
    @DisplayName("Token 정보를 올바르게 출력할 수 있다.")
    void whenToStringThenCorrectlyFormatted() {
        final DailygeToken token = new DailygeToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE, ACCESS_TOKEN_MAX_AGE, REFRESH_TOKEN_MAX_AGE);
        final String tokenString = token.toString();
        assertEquals("{accessToken: accessTokenValue, refreshToken: refreshTokenValue}", tokenString);
    }

    @Test
    @DisplayName("빈 값이 주어진 경우에도 쿠키를 올바르게 생성할 수 있다.")
    void whenTokensAreEmptyThenCorrectlyHandled() {
        final DailygeToken token = new DailygeToken("", "", ACCESS_TOKEN_MAX_AGE, REFRESH_TOKEN_MAX_AGE);
        final String accessTokenCookie = token.getAccessTokenCookie(ENV_PROD);
        final String refreshTokenCookie = token.getRefreshTokenCookie(ENV_PROD);
        assertAll(
            () -> assertTrue(accessTokenCookie.contains("Access-Token=")),
            () -> assertTrue(refreshTokenCookie.contains("Refresh-Token="))
        );
    }

    @Test
    @DisplayName("Token MaxAge가 0일 때 만료된 쿠키를 생성할 수 있다.")
    void whenMaxAgeIsZeroThenExpiredCookiesGenerated() {
        final DailygeToken token = new DailygeToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE, 0, 0);
        final String accessTokenCookie = token.getAccessTokenCookie(ENV_PROD);
        final String refreshTokenCookie = token.getRefreshTokenCookie(ENV_PROD);
        assertAll(
            () -> assertTrue(accessTokenCookie.contains("Max-Age=0")),
            () -> assertTrue(refreshTokenCookie.contains("Max-Age=0"))
        );
    }
}
