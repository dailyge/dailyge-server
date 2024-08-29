package project.dailyge.app.core.common.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.app.common.auth.TokenProvider;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.common.utils.CookieUtils.clearCookie;

@Component
@RequiredArgsConstructor
public class BlacklistInterceptor implements HandlerInterceptor {

    private final UserBlacklistService userBlacklistService;
    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) {
        try {
            final String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null) {
                return true;
            }
            if (authorizationHeader.isBlank()) {
                return true;
            }
            final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
            if (userBlacklistService.existsByAccessToken(accessToken)) {
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
        response.setStatus(INVALID_USER_TOKEN.code());
    }
}
