package project.dailyge.app.core.common.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.core.cache.user.UserBlacklistReadUseCase;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.common.utils.CookieUtils.clearCookie;

@Component
@RequiredArgsConstructor
public class BlacklistInterceptor implements HandlerInterceptor {

    private final UserBlacklistReadUseCase userBlacklistReadUseCase;

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) {
        try {
            final Cookies cookies = new Cookies(request.getCookies());
            final String accessToken = cookies.getValueByKey("Access-Token");
            if (userBlacklistReadUseCase.existsByAccessToken(accessToken)) {
                setLogoutResponse(response);
                return false;
            }
            return true;
        } catch (Exception ex) {
            return true;
        }
    }

    private void setLogoutResponse(final HttpServletResponse response) {
        response.addCookie(clearCookie("Refresh-Token"));
        response.addCookie(clearCookie("Access-Token"));
        response.setStatus(INVALID_USER_TOKEN.code());
    }
}
