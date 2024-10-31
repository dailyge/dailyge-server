package project.dailyge.app.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.annotation.ExternalLayer;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.annotation.PresentationLayer;
import static project.dailyge.app.constant.LogConstant.INFO;
import static project.dailyge.app.constant.LogConstant.IN_COMING;
import static project.dailyge.app.constant.LogConstant.IP;
import static project.dailyge.app.constant.LogConstant.LOG_ORDER;
import static project.dailyge.app.constant.LogConstant.METHOD;
import static project.dailyge.app.constant.LogConstant.OUT_GOING;
import static project.dailyge.app.constant.LogConstant.PATH;
import static project.dailyge.app.constant.LogConstant.TRACE_ID;
import static project.dailyge.app.constant.LogConstant.USER_ID;
import static project.dailyge.app.utils.LogUtils.createLogMessage;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

@Aspect
@Component
@Profile("!test")
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String EMPTY_STRING = "";
    private static final String EMPTY_ARRAY = "[]";
    private static final String ANNOTATION_VALUE = "value";

    private final ObjectMapper objectMapper;

    public LoggingAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("@within(project.dailyge.app.common.annotation.PresentationLayer)")
    public Object writeLogAroundPresentation(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String layer = getLayer(joinPoint, PresentationLayer.class);
        return writeLog(joinPoint, layer);
    }

    @Around("@within(project.dailyge.app.common.annotation.FacadeLayer)")
    public Object writeLogAroundFacade(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String layer = getLayer(joinPoint, FacadeLayer.class);
        return writeLog(joinPoint, layer);
    }

    @Around("@within(project.dailyge.app.common.annotation.ApplicationLayer)")
    public Object writeLogAroundApplication(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String layer = getLayer(joinPoint, ApplicationLayer.class);
        return writeLog(joinPoint, layer);
    }

    @Around("@within(project.dailyge.app.common.annotation.ExternalLayer)")
    public Object writeLogAroundExternal(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String layerName = getLayer(joinPoint, ExternalLayer.class);
        return writeLog(joinPoint, layerName);
    }

    private <T extends Annotation> String getLayer(
        final ProceedingJoinPoint joinPoint,
        final Class<T> clazz
    ) {
        final T annotation = getAnnotation(joinPoint, clazz);
        try {
            return (String) clazz.getMethod(ANNOTATION_VALUE).invoke(annotation);
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    private Object writeLog(
        final ProceedingJoinPoint joinPoint,
        final String layer
    ) throws Throwable {
        final LocalDateTime startTime = LocalDateTime.now();
        logStart(joinPoint, format("%s%s", layer, IN_COMING), startTime);
        final Object result = joinPoint.proceed();
        final LocalDateTime endTime = LocalDateTime.now();
        logEnd(joinPoint, format("%s%s", layer, OUT_GOING), startTime, endTime, result);
        return result;
    }

    private void logStart(
        final ProceedingJoinPoint joinPoint,
        final String layer,
        final LocalDateTime startTime
    ) {
        try {
            final String traceId = MDC.get(TRACE_ID);
            final String ip = MDC.get(IP);
            final String method = MDC.get(METHOD);
            final String path = MDC.get(PATH);
            final String visitor = MDC.get(USER_ID);
            final Object[] argsArray = joinPoint.getArgs();
            final String args = (argsArray != null) ? objectMapper.writeValueAsString(argsArray) : EMPTY_ARRAY;
            increaseOrder();
            final int order = Integer.parseInt(MDC.get(LOG_ORDER));
            log.info(
                createLogMessage(order, layer, path, method, traceId, ip, startTime, 0, args, null, visitor, INFO)
            );
        } catch (Exception ex) {
            log.error("Logging failed");
        }
    }

    private void logEnd(
        final ProceedingJoinPoint joinPoint,
        final String layer,
        final LocalDateTime startTime,
        final LocalDateTime endTime,
        final Object result
    ) {
        try {
            final String traceId = MDC.get(TRACE_ID);
            final String ip = MDC.get(IP);
            final String method = MDC.get(METHOD);
            final String path = MDC.get(PATH);
            final String visitor = MDC.get(USER_ID);
            final Object[] argsArray = joinPoint.getArgs();
            final String args = (argsArray != null) ? objectMapper.writeValueAsString(argsArray) : EMPTY_ARRAY;
            final long duration = MILLIS.between(startTime, endTime);
            increaseOrder();
            final int order = Integer.parseInt(MDC.get(LOG_ORDER));
            log.info(
                createLogMessage(order, layer, path, method, traceId, ip, endTime, duration, args, result, visitor, INFO)
            );
        } catch (Exception ex) {
            log.error("Logging failed");
        }
    }

    private void increaseOrder() {
        try {
            final String logOrder = MDC.get(LOG_ORDER);
            final int order = logOrder != null ? Integer.parseInt(logOrder) : 0;
            MDC.put(LOG_ORDER, String.valueOf(order + 1));
        } catch (Exception ex) {
            log.error("Ordering failed");
        }
    }

    private <T extends Annotation> T getAnnotation(
        final ProceedingJoinPoint joinPoint,
        final Class<T> clazz
    ) {
        return ((Class<?>) joinPoint.getSignature()
            .getDeclaringType())
            .getAnnotation(clazz);
    }
}
