package project.dailyge.app.common.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.utils.LogUtils;
import static project.dailyge.app.utils.LogUtils.getGuest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Profile("!test")
public class LoggingAspect {

    private static final String SERVER = "dailyge-api";
    private static final String PRESENTATION = "PRESENTATION";
    private static final String FACADE = "FACADE";
    private static final String APPLICATION = "APPLICATION";
    private static final String EXTERNAL = "EXTERNAL";
    private static final String EVENT = "EVENT";
    private static final String TRACE_ID = "traceId";
    private static final String IP = "ip";
    private static final int START_DURATION = 0;

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
        final HttpServletRequest request = getRequest();
        final String traceId = MDC.get(TRACE_ID);
        final LocalDateTime startTime = LocalDateTime.now();
        final String args = Arrays.toString(joinPoint.getArgs());
        final String beforeLog = LogUtils.createLogMessage(
            SERVER,
            getPath(request),
            request.getMethod(),
            traceId,
            getIp(),
            layer,
            getDailygeUserAsString(request),
            startTime,
            START_DURATION,
            args,
            null
        );
        log.info(beforeLog);

        final Object result = joinPoint.proceed();
        final LocalDateTime endTime = LocalDateTime.now();
        final long duration = ChronoUnit.MILLIS.between(startTime, endTime);
        final String afterLog = LogUtils.createLogMessage(
            SERVER,
            getPath(request),
            request.getMethod(),
            traceId,
            getIp(),
            layer,
            getDailygeUserAsString(request),
            endTime,
            duration,
            args,
            result
        );
        log.info(afterLog);
        return result;
    }

    private String getIp() {
        return MDC.get(IP);
    }

    private String getDailygeUserAsString(final HttpServletRequest request) {
        final Object dailyObj = request.getAttribute("dailyge-user");
        if (dailyObj != null) {
            final DailygeUser dailygeUser = (DailygeUser) dailyObj;
            return dailygeUser.toString();
        }
        return getGuest();
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private String getPath(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}
