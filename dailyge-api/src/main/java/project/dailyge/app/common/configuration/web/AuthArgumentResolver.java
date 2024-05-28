package project.dailyge.app.common.configuration.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.INVALID_USER_ID;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.domain.user.UserJpaEntity;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DAILYGE_USER_ID = "dailyge_user_id";

    private final UserReadUseCase userReadUseCase;

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
        final String userId = request.getHeader(DAILYGE_USER_ID);
        validateUserId(userId);

        final UserJpaEntity findUser = userReadUseCase.findById(Long.parseLong(userId));
        return new DailygeUser(findUser);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            final String detailMessage = String.format("사용자 아이디가 존재하지 않습니다. 입력된 값:%s", userId);
            throw new UnAuthorizedException(detailMessage, UN_AUTHORIZED);
        }

        try {
            Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            final String detailMessage = String.format("올바르지 않은 사용자 아이디 입니다. 입력된 값:%s", userId);
            throw new UnAuthorizedException(detailMessage, INVALID_USER_ID);
        }
    }
}
