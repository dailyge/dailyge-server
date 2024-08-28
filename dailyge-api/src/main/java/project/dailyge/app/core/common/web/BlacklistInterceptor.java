package project.dailyge.app.core.common.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.core.cache.user.UserBlacklistReadUseCase;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;

@Component
@RequiredArgsConstructor
public class BlacklistInterceptor implements HandlerInterceptor {

    private final UserBlacklistReadUseCase userBlacklistReadUseCase;
    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader == null) {
            return true;
        }
        if (authorizationHeader.isBlank()) {
            return true;
        }
        final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
        final Long userId = tokenProvider.getUserId(accessToken);
        final Date blacklistDate = userBlacklistReadUseCase.getBlacklistById(userId);
        if (blacklistDate == null) {
            return true;
        }
        if (blacklistDate.after(tokenProvider.getIssuedAt(accessToken))) {
            setLogoutResponse(response);
            return false;
        }
        return true;
    }

    private void setLogoutResponse(final HttpServletResponse response) {
        Cookie refreshToken = new Cookie("Refresh-Token", null);
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(true);
        refreshToken.setDomain(".dailyge.com");
        refreshToken.setPath("/");
        refreshToken.setMaxAge(0);
        response.addCookie(refreshToken);
        response.setStatus(INVALID_USER_TOKEN.code());
    }
}
