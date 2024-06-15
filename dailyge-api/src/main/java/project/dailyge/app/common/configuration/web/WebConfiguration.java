package project.dailyge.app.common.configuration.web;

import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.*;
import org.springframework.web.servlet.config.annotation.*;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.user.application.*;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final UserReadUseCase userReadUseCase;
    private final TokenProvider tokenProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(userReadUseCase, tokenProvider));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
