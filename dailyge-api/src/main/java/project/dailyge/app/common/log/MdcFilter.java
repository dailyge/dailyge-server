package project.dailyge.app.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import static java.time.temporal.ChronoUnit.MILLIS;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import static project.dailyge.app.common.auth.DailygeUser.getDailygeUser;
import static project.dailyge.app.common.utils.IpUtils.extractIpAddress;
import static project.dailyge.app.constant.LogConstant.ENTRANCE_LAYER;
import static project.dailyge.app.constant.LogConstant.INFO;
import static project.dailyge.app.constant.LogConstant.IN_COMING;
import static project.dailyge.app.constant.LogConstant.IP;
import static project.dailyge.app.constant.LogConstant.LOG_ORDER;
import static project.dailyge.app.constant.LogConstant.LOG_ORDER_INI;
import static project.dailyge.app.constant.LogConstant.METHOD;
import static project.dailyge.app.constant.LogConstant.OUT_GOING;
import static project.dailyge.app.constant.LogConstant.PATH;
import static project.dailyge.app.constant.LogConstant.TRACE_ID;
import static project.dailyge.app.utils.LogUtils.createGuestLogMessage;
import static project.dailyge.app.utils.LogUtils.createLogMessage;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Order(1)
@Component
@Profile("!test")
public class MdcFilter implements Filter {

    private static final String DAILYGE_USER = "dailyge-user";

    @Override
    public void doFilter(
        final ServletRequest servletRequest,
        final ServletResponse servletResponse,
        final FilterChain filterChain
    ) throws IOException, ServletException {
        final String traceId = createTimeBasedUUID();
        final String userIp = extractIpAddress((HttpServletRequest) servletRequest);
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String path = request.getRequestURI();
        final String method = request.getMethod();
        final LocalDateTime startTime = LocalDateTime.now();
        try {
            writeIncomingLog(traceId, userIp, method, path, startTime);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            writeOutgoingLog(servletRequest, startTime, path, method, traceId, userIp);
        }
    }

    private static void writeIncomingLog(
        final String traceId,
        final String userIp,
        final String method,
        final String path,
        final LocalDateTime startTime
    ) {
        try {
            MDC.put(TRACE_ID, traceId);
            MDC.put(IP, userIp);
            MDC.put(LOG_ORDER, LOG_ORDER_INI);
            MDC.put(METHOD, method);
            MDC.put(PATH, path);
            log.info(createIncomingLog(path, method, traceId, userIp, startTime));
        } catch (Exception ex) {
            log.error("Incoming logging failed.");
        }
    }

    private static void writeOutgoingLog(
        final ServletRequest servletRequest,
        final LocalDateTime startTime,
        final String path,
        final String method,
        final String traceId,
        final String userIp
    ) {
        try {
            final LocalDateTime endTime = LocalDateTime.now();
            final long duration = MILLIS.between(startTime, endTime);

            final DailygeUser dailygeUser = getDailygeUser(servletRequest.getAttribute(DAILYGE_USER));
            final String longMessage = createOutgoingLog(path, method, traceId, userIp, endTime, duration, dailygeUser);
            servletRequest.removeAttribute(DAILYGE_USER);
            log.info(longMessage);
            MDC.clear();
        } catch (Exception ex) {
            log.error("Outgoing logging failed.");
        }
    }

    private static String createIncomingLog(
        final String path,
        final String method,
        final String traceId,
        final String userIp,
        final LocalDateTime startTime
    ) {
        return createGuestLogMessage(
            increaseAndGet(),
            String.format("%s%s", ENTRANCE_LAYER, IN_COMING),
            path,
            method,
            traceId,
            userIp,
            startTime,
            0,
            null,
            null,
            INFO
        );
    }

    private static String createOutgoingLog(
        final String path,
        final String method,
        final String traceId,
        final String userIp,
        final LocalDateTime endTime,
        final long duration,
        final DailygeUser dailygeUser
    ) throws JsonProcessingException {
        return createLogMessage(
            increaseAndGet(),
            String.format("%s%s", ENTRANCE_LAYER, OUT_GOING),
            path,
            method,
            traceId,
            userIp,
            endTime,
            duration,
            null,
            null,
            dailygeUser != null ? dailygeUser.toString() : null,
            INFO
        );
    }

    private static int increaseAndGet() {
        final String logOrder = MDC.get(LOG_ORDER);
        final int order = (logOrder != null) ? Integer.parseInt(logOrder) : 0;
        final int next = order + 1;
        MDC.put(LOG_ORDER, String.valueOf(next));
        return next;
    }
}
