package project.dailyge.app.core.common.web;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.dailyge.app.common.annotation.CursorPageable;
import project.dailyge.app.paging.Cursor;
import project.dailyge.app.paging.CursorFactory;

@Order(2)
public class CursorPagingArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String INDEX = "index";
    private static final String LIMIT = "limit";

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CursorPageable.class);
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        return createCursor(webRequest);
    }

    private Cursor createCursor(final NativeWebRequest webRequest) {
        return CursorFactory.createCursor(
            webRequest.getParameter(INDEX),
            webRequest.getParameter(LIMIT)
        );
    }
}
