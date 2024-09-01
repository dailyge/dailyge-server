package project.dailyge.app.common.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtils {

    public static Cookie clearCookie(final String name) {
        final Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(".dailyge.com");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
