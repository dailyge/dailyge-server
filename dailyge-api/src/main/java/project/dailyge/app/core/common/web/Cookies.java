package project.dailyge.app.core.common.web;

import jakarta.servlet.http.Cookie;

import java.util.HashMap;
import java.util.Map;

public final class Cookies {

    private final Map<String, String> cookieMap = new HashMap<>();

    public Cookies(final Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie.getValue());
        }
    }

    public String getValueByKey(final String key) {
        return cookieMap.get(key);
    }
}
