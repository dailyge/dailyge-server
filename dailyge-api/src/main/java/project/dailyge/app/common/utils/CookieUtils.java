package project.dailyge.app.common.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

    private static final String DOMAIN = ".dailyge.com";

    public static String createResponseCookie(
        final String name,
        final String value,
        final String path,
        final long maxAge
    ) {
        final ResponseCookie cookie =  ResponseCookie.from(name, value)
            .domain(DOMAIN)
            .path(path)
            .httpOnly(true)
            .secure(true)
            .maxAge(maxAge)
            .build();
        return cookie.toString();
    }

    public static String clearResponseCookie(final String name) {
        final ResponseCookie clearCookie = ResponseCookie.from(name, null)
            .domain(DOMAIN)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .maxAge(0)
            .build();
        return clearCookie.toString();
    }

    public static Cookie createCookie(
        final String name,
        final String value,
        final String path,
        final int maxAge
    ) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setDomain(DOMAIN);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie clearCookie(final String name) {
        final Cookie cookie = new Cookie(name, null);
        cookie.setDomain(DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}
