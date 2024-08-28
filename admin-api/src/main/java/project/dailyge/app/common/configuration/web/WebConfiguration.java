package project.dailyge.app.common.configuration.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.core.cache.user.UserCacheReadUseCase;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final UserCacheReadUseCase userCacheReadUseCase;
    private final TokenProvider tokenProvider;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(userCacheReadUseCase, tokenProvider));
    }
}
