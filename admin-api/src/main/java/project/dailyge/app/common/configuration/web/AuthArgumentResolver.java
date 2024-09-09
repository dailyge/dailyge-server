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
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadUseCase;
import project.dailyge.entity.user.Role;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserCacheReadUseCase userCacheReadUseCase;
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
        try {
            final Cookies cookies = new Cookies(request.getCookies());
            final String accessToken = cookies.getValueByKey("Access-Token");
            final Long userId = tokenProvider.getUserId(accessToken);
            if (userId == null) {
                throw CommonException.from(UN_AUTHORIZED);
            }
            final UserCache user = userCacheReadUseCase.findById(userId);
            final DailygeUser dailygeUser = new DailygeUser(user.getId(), Role.valueOf(user.getRole()));
            request.setAttribute("dailyge-user", dailygeUser);
            return dailygeUser;
        } catch (ExpiredJwtException ex) {
            throw CommonException.from(ex.getMessage(), INVALID_USER_TOKEN);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), UN_AUTHORIZED);
        }
    }
}
