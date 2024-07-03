package project.dailyge.app.common.configuration.web;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.JWTAuthenticationException;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.entity.user.UserJpaEntity;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.app.common.exception.JWTAuthenticationException.EMPTY_TOKEN_ERROR_MESSAGE;
import static project.dailyge.app.common.exception.JWTAuthenticationException.EXPIRED_TOKEN_ERROR_MESSAGE;

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
        if (authorizationHeader == null) {
            throw new UnAuthorizedException(EMPTY_TOKEN_ERROR_MESSAGE, UN_AUTHORIZED);
        }
        final String accessToken = tokenProvider.getAccessToken(authorizationHeader);
        try {
            final Long userId = tokenProvider.getUserId(accessToken);
            if (userId == null) {
                final String detailMessage = String.format("사용자 아이디가 존재하지 않습니다. 입력된 값:%s", userId);
                throw new UnAuthorizedException(detailMessage, UN_AUTHORIZED);
            }
            final UserJpaEntity findUser = userReadUseCase.findAuthorizedById(userId);

            return new DailygeUser(findUser);
        } catch (ExpiredJwtException e) {
            throw new JWTAuthenticationException(EXPIRED_TOKEN_ERROR_MESSAGE, UN_AUTHORIZED);
        }
    }
}
