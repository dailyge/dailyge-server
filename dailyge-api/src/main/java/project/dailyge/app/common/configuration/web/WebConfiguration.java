package project.dailyge.app.common.configuration.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.common.web.BlacklistInterceptor;
import project.dailyge.app.core.common.web.CouponApplyInterceptor;
import project.dailyge.app.core.common.web.CursorPagingArgumentResolver;
import project.dailyge.app.core.common.web.LoginInterceptor;
import project.dailyge.core.cache.user.UserCacheReadUseCase;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final TokenProvider tokenProvider;
    private final LoginInterceptor loginInterceptor;
    private final BlacklistInterceptor blacklistInterceptor;
    private final CouponApplyInterceptor couponApplyInterceptor;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(userCacheReadUseCase, tokenProvider));
        resolvers.add(new CursorPagingArgumentResolver());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/api/login");
        registry.addInterceptor(blacklistInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(couponApplyInterceptor).addPathPatterns("/api/coupons");
    }

    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
