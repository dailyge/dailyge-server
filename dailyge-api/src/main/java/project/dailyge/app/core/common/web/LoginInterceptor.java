package project.dailyge.app.core.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.external.oauth.TokenManager;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final TokenProvider tokenProvider;
    private final TokenManager tokenManager;
    private final ObjectMapper objectMapper;

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
            final Long userId = tokenProvider.getUserId(accessToken);
            if (!userCacheReadUseCase.existsById(userId)) {
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
            if (refreshToken == null) {
                return true;
            }

            final Claims claims = expiredJwtException.getClaims();
            final String encryptedUserId = claims.get("id", String.class);
            final Long userId = tokenProvider.decryptUserId(encryptedUserId);
            if (!userCacheReadUseCase.existsById(userId)) {
                return true;
            }
            if (!tokenManager.getRefreshToken(userId).equals(refreshToken)) {
                return true;
            }

            final UserCache userCache = userCacheReadUseCase.findById(userId);
            final DailygeToken token = tokenProvider.createToken(userCache.getId(), userCache.getEmail());

            setLoggedInResponse(request, response, token.accessToken());
            return false;
        } catch (Exception ex) {
            log.error("refresh token error", ex);
            return true;
        }
    }

    private String getRefreshToken(final HttpServletRequest request) {
        final Cookies cookies = new Cookies(request.getCookies());
        return cookies.getValueByKey("Refresh-Token");
    }

    private void setLoggedInResponse(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String accessToken
    ) throws IOException {
        final Map<String, String> bodyMap = new HashMap<>();
        final String referer = request.getHeader("referer");
        bodyMap.put("url", referer == null ? "/" : referer);
        bodyMap.put("Access-Token", accessToken);

        response.setStatus(BAD_REQUEST.code());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), bodyMap);
    }
}
