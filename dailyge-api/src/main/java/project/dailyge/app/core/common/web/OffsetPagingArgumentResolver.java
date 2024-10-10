package project.dailyge.app.core.common.web;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.dailyge.app.common.annotation.OffsetPageable;
import project.dailyge.app.page.Page;
import project.dailyge.app.page.PageFactory;

@Order(3)
public class OffsetPagingArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OffsetPageable.class);
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        return createPage(webRequest);
    }

    private Page createPage(final NativeWebRequest webRequest) {
        return PageFactory.createPage(
            webRequest.getParameter(PAGE),
            webRequest.getParameter(LIMIT)
        );
    }
}
