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
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;
import static project.dailyge.app.common.utils.CookieUtils.createCookie;

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
            final Cookies cookies = new Cookies(request.getCookies());
            final String accessToken = cookies.getValueByKey("Access-Token");
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
            final Claims claims = expiredJwtException.getClaims();
            final String encryptedUserId = claims.get("id", String.class);
            final Long userId = tokenProvider.decryptUserId(encryptedUserId);
            final UserCache userCache = userCacheReadUseCase.findById(userId);
            if (userCache == null) {
                return true;
            }
            final Cookies cookies = new Cookies(request.getCookies());
            final String refreshToken = cookies.getValueByKey("Refresh-Token");
            if (!tokenManager.getRefreshToken(userId).equals(refreshToken)) {
                return true;
            }

            final DailygeToken token = tokenProvider.createToken(userCache.getId());
            setLoggedInResponse(request, response, token.accessToken());
            return false;
        } catch (Exception ex) {
            log.error("refresh token error", ex);
            return true;
        }
    }

    private void setLoggedInResponse(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final String accessToken
    ) throws IOException {
        final Map<String, String> bodyMap = new HashMap<>();
        final String referer = request.getHeader("referer");
        bodyMap.put("url", referer == null ? "/" : referer);
        response.addCookie(createCookie("Access-Token", accessToken, "/", 900));
        response.setStatus(BAD_REQUEST.code());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), bodyMap);
    }
}
