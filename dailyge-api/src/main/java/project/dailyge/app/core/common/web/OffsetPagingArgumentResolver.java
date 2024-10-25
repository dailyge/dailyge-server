package project.dailyge.app.core.common.web;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.dailyge.app.common.annotation.OffsetPageable;
import project.dailyge.app.paging.CustomPageable;
import project.dailyge.app.paging.PageFactory;

@Order(3)
public class OffsetPagingArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";

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

    private CustomPageable createPage(final NativeWebRequest webRequest) {
        return PageFactory.createPage(
            webRequest.getParameter(PAGE_NUMBER),
            webRequest.getParameter(PAGE_SIZE)
        );
    }
}
