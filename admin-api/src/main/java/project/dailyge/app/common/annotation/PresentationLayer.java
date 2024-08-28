package project.dailyge.app.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Retention(RetentionPolicy.RUNTIME)
public @interface PresentationLayer {
}
