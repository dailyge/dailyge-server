package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.common.web.Cookies;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[UnitTest] Cookies 단위 테스트")
class CookiesUnitTest {

    private static final String KEY = "dailyge";
    private static final String VALUE = "good";
    private static final String LOGGED_IN = "Logged-In";
    private Cookie[] cookie;

    @BeforeEach
    void setUp() {
        cookie = new Cookie[1];
    }

    @Test
    @DisplayName("존재하는 쿠키의 값을 꺼내면, 쿠키 값이 반환한다.")
    void whenExistsCookieThenResultShouldBeNotNull() {
        cookie[0] = new Cookie(KEY, VALUE);
        final Cookies cookies = new Cookies(cookie);

        assertEquals(VALUE, cookies.getValueByKey(KEY));
    }

    @Test
    @DisplayName("존재하지 않은 쿠키의 값을 꺼내면, Null을 반환한다.")
    void whenNonExistsCookieThenResultShouldBeNull() {
        cookie[0] = new Cookie(KEY, VALUE);
        final Cookies cookies = new Cookies(cookie);

        assertNull(cookies.getValueByKey("test"));
    }

    @Test
    @DisplayName("로그인 된 사용자는, true를 반환한다.")
    void whenLoggedInUserThenResultShouldBeTrue() {
        cookie[0] = new Cookie(LOGGED_IN, "yes");
        final Cookies cookies = new Cookies(cookie);

        assertTrue(cookies.isLoggedIn());
    }

    @Test
    @DisplayName("미 로그인 사용자는, false를 반환한다.")
    void whenNotLoggedInUserThenResultShouldBeTrue() {
        cookie[0] = new Cookie(LOGGED_IN, "no");
        final Cookies cookies = new Cookies(cookie);

        assertFalse(cookies.isLoggedIn());
    }

    @Test
    @DisplayName("Logged-In이 비어있는 요청은, false를 반환한다.")
    void whenLoggedInIsEmptyThenResultShouldBeFalse() {
        final Cookie[] emtpyCookie = new Cookie[0];
        final Cookies cookies = new Cookies(emtpyCookie);

        assertFalse(cookies.isLoggedIn());
    }
}
