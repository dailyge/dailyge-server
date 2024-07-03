package project.dailyge.app.test.common;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.core.common.web.Cookies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("[UnitTest] Cookies 단위 테스트")
class CookiesUnitTest {

    private static final String KEY = "dailyge";
    private static final String VALUE = "good";

    @Test
    @DisplayName("존재하는 쿠키의 값을 꺼내면, 쿠키 값이 반환한다.")
    void whenExistsCookieThenResultShouldBeNotNull() {
        final Cookie[] cookie = new Cookie[1];
        cookie[0] = new Cookie(KEY, VALUE);
        final Cookies cookies = new Cookies(cookie);

        assertEquals(VALUE, cookies.getValueByKey(KEY));
    }

    @Test
    @DisplayName("존재하지 않은 쿠키의 값을 꺼내면, Null을 반환한다.")
    void whenNonExistsCookieThenResultShouldBeNull() {
        final Cookie[] cookie = new Cookie[1];
        cookie[0] = new Cookie(KEY, VALUE);
        final Cookies cookies = new Cookies(cookie);

        assertNull(cookies.getValueByKey("test"));
    }
}
