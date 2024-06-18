package project.dailyge.app.common.configuration.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.domain.user.UserJpaEntity;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = getToken(request);
        if (tokenProvider.isValidToken(accessToken)) {
            Long userId = tokenProvider.getUserId(accessToken);
            validateUserId(userId);

            final UserJpaEntity findUser = userReadUseCase.findAuthorizedById(userId);
            return new DailygeUser(findUser);
        }
        return null;
    }

    private String getToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        return accessToken;
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            final String detailMessage = String.format("사용자 아이디가 존재하지 않습니다. 입력된 값:%s", userId);
            throw new UnAuthorizedException(detailMessage, UN_AUTHORIZED);
        }
    }
}
