package project.dailyge.app.common.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtils {

    public static Cookie deleteCookie(final String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain(".dailyge.com");
        cookie.setPath("/");
        cookie.setMaxAge(3000);
        return cookie;
    }
}
