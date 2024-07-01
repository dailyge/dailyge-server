package project.dailyge.app.core.common.web;

import com.nimbusds.jose.shaded.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.entity.user.UserJpaEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private static final String LOGIN_URI = "/api/login";
    private static final String DEFAULT_REFERER = "/";
    private static final String REFERER = "referer";
    private static final String REFRESH_TOKEN = "Refresh-Token";
    private static final String ACCESS_TOKEN = "accessToken";

    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) {
        try {
            if (!LOGIN_URI.equals(request.getRequestURI())) {
                return true;
            }
            final String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null) {
                return true;
            }
            final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
            tokenProvider.validateToken(accessToken);
            final Long userId = tokenProvider.getUserId(accessToken);
            userReadUseCase.findActiveUserById(userId);
            setLoggedResponse(request, response, accessToken);
            return false;
        } catch (ExpiredJwtException ex) {
            return refreshToken(request, response, ex);
        } catch (Exception ex) {
            log.error("abnormal token error: {}", ex);
            return true;
        }
    }

    private boolean refreshToken(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final ExpiredJwtException expiredJwtException
    ) {
        try {
            Optional<Cookie> cookieStream = Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                .findFirst();
            if (cookieStream.isEmpty()) {
                return true;
            }
            final Cookie cookie = cookieStream.get();
            final String refreshToken = cookie.getValue();
            final Claims claims = expiredJwtException.getClaims();
            final Long userId = claims.get("id", Long.class);
            userReadUseCase.findActiveUserById(userId);
            if (!tokenManager.getRefreshTokenKey(userId).equals(refreshToken)) {
                return true;
            }
            final UserJpaEntity user = userReadUseCase.findActiveUserById(userId);
            final DailygeToken token = tokenProvider.createToken(user);
            setLoggedResponse(request, response, token.accessToken());
            return false;
        } catch (Exception ex) {
            log.error("refresh token error", ex);
            return true;
        }
    }

    private void setLoggedResponse(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String accessToken
    ) throws IOException {
        final HashMap<String, String> jsonBody = new HashMap<>();
        final String referer = request.getHeader(REFERER);
        jsonBody.put("url", referer == null ? DEFAULT_REFERER : referer);
        jsonBody.put(ACCESS_TOKEN, accessToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final PrintWriter writer = response.getWriter();
        final Gson gson = new Gson();
        writer.print(gson.toJson(jsonBody));
    }
}
