package project.dailyge.app.common.log;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import static org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes;

import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(@NonNull final Runnable runnable) {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        final Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(requestAttributes, true);
                }
                if (context != null) {
                    MDC.setContextMap(context);
                }
                runnable.run();
            } finally {
                resetRequestAttributes();
                MDC.clear();
            }
        };
    }
}
