package project.dailyge.app.common.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

public final class CookieUtils {

    private static final String ROOT_DOMAIN = ".dailyge.com";

    private CookieUtils() {
        throw new AssertionError("올바른 형식으로 생성자를 호출해주세요.");
    }

    public static String createResponseCookie(
        final String name,
        final String value,
        final String path,
        final long maxAge,
        final boolean httpOnly,
        final String env
    ) {
        if (!"local".equals(env)) {
            final ResponseCookie cookie = ResponseCookie.from(name, value)
                .domain(ROOT_DOMAIN)
                .path(path)
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .build();
            return cookie.toString();
        }
        final ResponseCookie cookie = ResponseCookie.from(name, value)
            .domain("")
            .path(path)
            .httpOnly(httpOnly)
            .maxAge(maxAge)
            .build();
        return cookie.toString();
    }

    public static String clearResponseCookie(
        final String name,
        final boolean httpOnly
    ) {
        final ResponseCookie clearCookie = ResponseCookie.from(name, null)
            .domain(ROOT_DOMAIN)
            .path("/")
            .httpOnly(false)
            .secure(false)
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
        cookie.setDomain(ROOT_DOMAIN);
        cookie.setPath(path);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Cookie clearCookie(final String name) {
        final Cookie cookie = new Cookie(name, null);
        cookie.setDomain(ROOT_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(0);
        return cookie;
    }
}
