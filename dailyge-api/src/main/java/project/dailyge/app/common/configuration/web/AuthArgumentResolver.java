package project.dailyge.app.common.configuration.web;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.entity.user.UserJpaEntity;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
        try {
            final Long userId = tokenProvider.getUserId(accessToken);
            if (userId == null) {
                throw new UnAuthorizedException();
            }
            final UserJpaEntity findUser = userReadUseCase.findAuthorizedUserById(userId);
            final DailygeUser dailygeUser = new DailygeUser(findUser.getId(), findUser.getRole());
            request.setAttribute("dailyge-user", dailygeUser);
            return dailygeUser;
        } catch (ExpiredJwtException ex) {
            throw new UnAuthorizedException(ex.getMessage(), INVALID_USER_TOKEN);
        } catch (Exception ex) {
            throw new UnAuthorizedException(ex.getMessage(), UN_AUTHORIZED);
        }
    }
}
