package project.dailyge.app.common.annotation;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RestController
@Retention(RetentionPolicy.RUNTIME)
public @interface Presentation {
}
