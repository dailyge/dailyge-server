package project.dailyge.app.common.configuration.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.core.cache.user.UserCacheReadService;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final UserCacheReadService userCacheReadService;
    private final TokenProvider tokenProvider;

    public WebConfiguration(
        final UserCacheReadService userCacheReadService,
        final TokenProvider tokenProvider
    ) {
        this.userCacheReadService = userCacheReadService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(userCacheReadService, tokenProvider));
    }
}
