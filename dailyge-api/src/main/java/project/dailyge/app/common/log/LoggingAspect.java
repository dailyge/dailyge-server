package project.dailyge.app.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import static java.time.temporal.ChronoUnit.MILLIS;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static project.dailyge.app.constant.LogConstant.APPLICATION;
import static project.dailyge.app.constant.LogConstant.EXTERNAL;
import static project.dailyge.app.constant.LogConstant.FACADE;
import static project.dailyge.app.constant.LogConstant.IP;
import static project.dailyge.app.constant.LogConstant.LAST_LAYER;
import static project.dailyge.app.constant.LogConstant.LOG_ORDER;
import static project.dailyge.app.constant.LogConstant.PRESENTATION;
import static project.dailyge.app.constant.LogConstant.TRACE_ID;
import static project.dailyge.app.utils.LogUtils.createLogMessage;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Profile("!test")
public class LoggingAspect {

    private static final String DAILYGE_USER_KEY = "dailyge-user";

    @Around("@within(project.dailyge.app.common.annotation.PresentationLayer)")
    public Object writeLogAroundPresentation(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, PRESENTATION);
    }

    @Around("@within(project.dailyge.app.common.annotation.FacadeLayer)")
    public Object writeLogAroundFacade(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, FACADE);
    }

    @Around("@within(project.dailyge.app.common.annotation.ApplicationLayer)")
    public Object writeLogAroundApplication(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, APPLICATION);
    }

    @Around("@within(project.dailyge.app.common.annotation.ExternalLayer)")
    public Object writeLogAroundExternal(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, EXTERNAL);
    }

    private Object writeLog(
        final ProceedingJoinPoint joinPoint,
        final String layer
    ) throws Throwable {
        final LocalDateTime startTime = LocalDateTime.now();
        final Object result = joinPoint.proceed();
        final LocalDateTime endTime = LocalDateTime.now();
        log(joinPoint, layer, startTime, endTime, result);
        return result;
    }

    private void log(
        final ProceedingJoinPoint joinPoint,
        final String layer,
        final LocalDateTime startTime,
        final LocalDateTime endTime,
        final Object result
    ) throws JsonProcessingException {
        final HttpServletRequest request = getCurrentHttpRequest();
        final String traceId = MDC.get(TRACE_ID);
        final String ip = MDC.get(IP);
        final String path = request.getRequestURI();
        final String method = request.getMethod();
        final String args = Arrays.toString(joinPoint.getArgs());
        long duration = MILLIS.between(startTime, endTime);

        if (MDC.get(LAST_LAYER) == null || !MDC.get(LAST_LAYER).equals(layer)) {
            increaseOrder();
            MDC.put(LAST_LAYER, layer);
        }
        int order = Integer.parseInt(MDC.get(LOG_ORDER));

        log.info(createLogMessage(order, layer, path, method, traceId, ip, startTime, 0, args, null, request.getAttribute(DAILYGE_USER_KEY)));
        log.info(createLogMessage(order, layer, path, method, traceId, ip, endTime, duration, args, result, request.getAttribute(DAILYGE_USER_KEY)));
    }

    private void increaseOrder() {
        final int order = Integer.parseInt(MDC.get(LOG_ORDER));
        MDC.put(LOG_ORDER, String.valueOf(order + 1));
    }

    private HttpServletRequest getCurrentHttpRequest() {
        final RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) attrs).getRequest();
        }
        throw new IllegalStateException("No current request attributes.");
    }
}
