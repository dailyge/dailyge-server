package project.dailyge.app.core.common.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.app.common.utils.CookieUtils;

import static org.springframework.http.HttpMethod.POST;

@Component
public class CouponApplyInterceptor implements HandlerInterceptor {

    private static final String IS_PARTICIPATED = "isParticipated";
    private static final int maxAge = 7 * 24 * 60 * 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(POST.name())) {
            createCouponApplyCookie(response);
        }
        return true;
    }

    private void createCouponApplyCookie(HttpServletResponse response) {
        Cookie cookie = CookieUtils.createCookie(IS_PARTICIPATED, "true", "/", maxAge);
        response.addCookie(cookie);
    }

}
