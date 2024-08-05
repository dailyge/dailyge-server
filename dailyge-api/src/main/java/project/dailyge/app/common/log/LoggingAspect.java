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
    private static final String TRACE_ID = "traceId";
    private static final String IP = "ip";

    @Around("@within(project.dailyge.app.common.annotation.Presentation)")
    public Object writeLogAroundPresentation(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, PRESENTATION);
    }

    @Around("@within(project.dailyge.app.common.annotation.Facade)")
    public Object writeLogAroundFacade(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, FACADE);
    }

    @Around("@within(project.dailyge.app.common.annotation.Application)")
    public Object writeLogAroundApplication(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, APPLICATION);
    }

    @Around("@within(project.dailyge.app.common.annotation.External)")
    public Object writeLogAroundExternal(final ProceedingJoinPoint joinPoint) throws Throwable {
        return writeLog(joinPoint, EXTERNAL);
    }

    private Object writeLog(
        final ProceedingJoinPoint joinPoint,
        final String layer
    ) throws Throwable {
        final String traceId = MDC.get(TRACE_ID);
        final LocalDateTime startTime = LocalDateTime.now();
        final String args = Arrays.toString(joinPoint.getArgs());
        final String beforeLog = LogUtils.createLogMessage(
            SERVER,
            traceId,
            getIp(),
            layer,
            getDailygeUserAsString(),
            startTime,
            0,
            args,
            null
        );
        log.info(beforeLog);

        final Object result = joinPoint.proceed();
        final LocalDateTime endTime = LocalDateTime.now();
        long duration = ChronoUnit.MILLIS.between(startTime, endTime);
        final String afterLog = LogUtils.createLogMessage(
            SERVER,
            traceId,
            getIp(),
            layer,
            getDailygeUserAsString(),
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

    private String getDailygeUserAsString() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final Object dailyObj = request.getAttribute("dailyge-user");
        if (dailyObj != null) {
            final DailygeUser dailygeUser = (DailygeUser) dailyObj;
            return dailygeUser.toString();
        }
        return getGuest();
    }
}
