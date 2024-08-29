package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.utils.CookieUtils;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[UnitTest] CookieUtils 단위 테스트")
class CookieUtilsUnitTest {

    private static final String REFRESH_TOKEN = "Refresh-Token";

    @Test
    @DisplayName("만료할 쿠키를 생성하면, 정상적으로 생성된다.")
    void whenCreateCookieToExpireThenMaxAgeShouldBeZero() {
        final Cookie cookie = CookieUtils.clearCookie(REFRESH_TOKEN);

        assertAll(
            () -> assertEquals(REFRESH_TOKEN, cookie.getName()),
            () -> assertEquals(0, cookie.getMaxAge()),
            () -> assertEquals(".dailyge.com", cookie.getDomain()),
            () -> assertEquals("/", cookie.getPath()),
            () -> assertTrue(cookie.getSecure()),
            () -> assertTrue(cookie.isHttpOnly()),
            () -> assertNull(cookie.getValue())

        );
    }
}
