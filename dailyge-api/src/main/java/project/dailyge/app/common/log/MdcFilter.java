package project.dailyge.app.common.log;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import static java.util.UUID.randomUUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import static project.dailyge.app.utils.LogUtils.createGuestLogMessage;
import static project.dailyge.app.utils.LogUtils.createLogMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Order(1)
@Component
public class MdcFilter implements Filter {

    private static final String SERVER = "dailyge-api";
    private static final String PATH = "path";
    private static final String DAILYGE_USER = "dailyge-user";
    private static final String ENTRANCE_LAYER = "ENTRANCE";
    private static final String TRACE_ID = "traceId";
    private static final String IP = "ip";
    private static final String UN_KNOWN = "unknown";

    @Override
    public void doFilter(
        final ServletRequest servletRequest,
        final ServletResponse servletResponse,
        final FilterChain filterChain
    ) throws IOException, ServletException {
        final String traceId = randomUUID().toString();
        final String userIp = getClientIpAddress((HttpServletRequest) servletRequest);
        final HttpServletRequest request = ((HttpServletRequest) servletRequest);
        final String path = request.getRequestURI();
        final String method = request.getMethod();
        final LocalDateTime startTime = LocalDateTime.now();

        try {
            MDC.put(TRACE_ID, traceId);
            MDC.put(IP, userIp);
            MDC.put(PATH, path);
            final String longMessage = createGuestLogMessage(SERVER, path, method, traceId, userIp, ENTRANCE_LAYER, startTime, 0, null, null);
            log.info(longMessage);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            final LocalDateTime endTime = LocalDateTime.now();
            final long duration = ChronoUnit.MILLIS.between(startTime, endTime);
            final DailygeUser dailygeUser = getDailygeUser(servletRequest);
            final String longMessage;
            if (dailygeUser != null) {
                longMessage = createLogMessage(SERVER, path, method, traceId, userIp, ENTRANCE_LAYER, dailygeUser.toString(), endTime, duration, null, null);
                servletRequest.removeAttribute(DAILYGE_USER);
            } else {
                longMessage = createLogMessage(SERVER, path, method, traceId, userIp, ENTRANCE_LAYER, endTime, duration, null, null);
            }
            log.info(longMessage);
            MDC.clear();
        }
    }

    private static DailygeUser getDailygeUser(final ServletRequest servletRequest) {
        try {
            final Object dailygeUser = servletRequest.getAttribute(DAILYGE_USER);
            if (dailygeUser != null) {
                final DailygeUser user = (DailygeUser) dailygeUser;
                servletRequest.removeAttribute(DAILYGE_USER);
                return user;
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private String getClientIpAddress(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
