package project.dailyge.app.common.configuration.web;

import jakarta.servlet.http.*;
import lombok.*;
import org.springframework.core.*;
import org.springframework.web.bind.support.*;
import org.springframework.web.context.request.*;
import org.springframework.web.method.support.*;
import project.dailyge.app.common.auth.*;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.*;
import project.dailyge.app.common.exception.*;
import project.dailyge.app.core.user.application.*;
import project.dailyge.domain.user.*;

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
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String userId = request.getHeader(DAILYGE_USER_ID);
        validateUserId(userId);

        User findUser = userReadUseCase.findById(Long.parseLong(userId));
        return new DailygeUser(findUser);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            String detailMessage = String.format("사용자 아이디가 존재하지 않습니다. 입력된 값:%s", userId);
            throw new UnAuthorizedException(detailMessage, UN_AUTHORIZED);
        }

        try {
            Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            String detailMessage = String.format("올바르지 않은 사용자 아이디 입니다. 입력된 값:%s", userId);
            throw new UnAuthorizedException(detailMessage, INVALID_USER_ID);
        }
    }
}
