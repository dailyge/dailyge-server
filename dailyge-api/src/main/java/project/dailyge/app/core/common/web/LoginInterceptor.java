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
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_REFERER = "/";
    private static final String REFERER = "referer";
    private static final String REFRESH_TOKEN = "Refresh-Token";
    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String URL = "url";
    private static final String UTF_8 = "UTF-8";

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
            final String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                return true;
            }

            final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
            tokenProvider.validateToken(accessToken);
            final Long userId = tokenProvider.getUserId(accessToken);
            if (!userReadUseCase.existsById(userId)) {
                return true;
            }

            setLoggedInResponse(request, response, accessToken);
            return false;
        } catch (ExpiredJwtException ex) {
            return refreshToken(request, response, ex);
        } catch (Exception ex) {
            log.error("abnormal token error", ex);
            return true;
        }
    }

    private boolean refreshToken(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final ExpiredJwtException expiredJwtException
    ) {
        try {
            final String refreshToken = getRefreshToken(request);
            if (refreshToken == null) return true;

            final Claims claims = expiredJwtException.getClaims();
            final Long userId = claims.get("id", Long.class);
            if (!userReadUseCase.existsById(userId) ||
                !tokenManager.getRefreshTokenKey(userId).equals(refreshToken)) {
                return true;
            }

            final UserJpaEntity user = userReadUseCase.findActiveUserById(userId);
            final DailygeToken token = tokenProvider.createToken(user);

            setLoggedInResponse(request, response, token.accessToken());
            return false;
        } catch (Exception ex) {
            log.error("refresh token error", ex);
            return true;
        }
    }

    private String getRefreshToken(final HttpServletRequest request) {
        final Optional<Cookie> cookieStream = Arrays.stream(request.getCookies())
            .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
            .findFirst();
        if (cookieStream.isEmpty()) {
            return null;
        }
        final Cookie cookie = cookieStream.get();
        return cookie.getValue();
    }

    private void setLoggedInResponse(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String accessToken
    ) throws IOException {
        final Map<String, String> bodyMap = new HashMap<>();
        final String referer = request.getHeader(REFERER);
        bodyMap.put(URL, referer == null ? DEFAULT_REFERER : referer);
        bodyMap.put(ACCESS_TOKEN, accessToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8);
        final PrintWriter writer = response.getWriter();
        final Gson gson = new Gson();
        writer.print(gson.toJson(bodyMap));
    }
}
