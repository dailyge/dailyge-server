package project.dailyge.app.common.configuration.web;

import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.web.method.support.*;
import org.springframework.web.servlet.config.annotation.*;
import project.dailyge.app.core.user.application.*;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final UserReadUseCase userReadUseCase;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(userReadUseCase));
    }
}
