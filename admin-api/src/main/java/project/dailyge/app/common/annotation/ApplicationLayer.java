package project.dailyge.app.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.stereotype.Service;

@Service
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationLayer {
}
