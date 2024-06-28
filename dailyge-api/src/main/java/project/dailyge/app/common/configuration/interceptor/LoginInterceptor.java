package project.dailyge.app.common.configuration.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.exception.UserTypeException;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private static final String LOGIN_URL = "/api/login";
    private static final String DEFAULT_REFERER = "/";
    private static final String REFERER = "referer";

    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) throws IOException {
        if (!LOGIN_URL.equals(request.getRequestURI())) {
            return true;
        }
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
        try {
            tokenProvider.validateToken(accessToken);
            final Long userId = tokenProvider.getUserId(accessToken);
            userReadUseCase.findActiveUserById(userId);

            final String referer = request.getHeader(REFERER);
            response.setStatus(CommonCodeAndMessage.FOUND.code());
            final PrintWriter writer = response.getWriter();
            writer.print(referer == null ? DEFAULT_REFERER : referer);
            return false;
        } catch (UnAuthorizedException | UserTypeException ex) {
            return true;
        }
    }
}
