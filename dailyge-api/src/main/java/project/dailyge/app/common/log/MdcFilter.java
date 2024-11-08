package project.dailyge.app.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import static java.time.temporal.ChronoUnit.MILLIS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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
import static project.dailyge.app.constant.LogConstant.USER_ID;
import static project.dailyge.app.utils.LogUtils.createGuestLogMessage;
import static project.dailyge.app.utils.LogUtils.createLogMessage;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.user.Role.GUEST;

import java.io.IOException;
import java.time.LocalDateTime;

@Order(1)
@Component
@Profile("!test")
public class MdcFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(MdcFilter.class);

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
            writeOutgoingLog(startTime, path, method, traceId, userIp);
            MDC.clear();
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
        final LocalDateTime startTime,
        final String path,
        final String method,
        final String traceId,
        final String userIp
    ) {
        try {
            final LocalDateTime endTime = LocalDateTime.now();
            final long duration = MILLIS.between(startTime, endTime);

            final String userId = MDC.get(USER_ID);
            final String longMessage = createOutgoingLog(
                path, method, traceId, userIp, endTime, duration, userId != null ? userId : GUEST.name()
            );
            log.info(longMessage);
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
        final String userId
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
            userId,
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
