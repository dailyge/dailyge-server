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
// @Component
public class MdcFilter implements Filter {

    private static final String[] IP_HEADERS = {
        "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };
    private static final String DELIMITER = ",";
    private static final String SERVER = "dailyge-api";
    private static final String DAILYGE_USER = "dailyge-user";
    private static final String ENTRANCE_LAYER = "ENTRANCE";
    private static final String TRACE_ID = "traceId";
    private static final String IP = "ip";
    private static final String UN_KNOWN = "unknown";

    private static final int ORIGIN = 0;

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
        for (final String header : IP_HEADERS) {
            final String ips = request.getHeader(header);
            if (ips != null && !ips.isEmpty() && !UN_KNOWN.equalsIgnoreCase(ips)) {
                try {
                    return ips.split(DELIMITER)[ORIGIN];
                } catch (Exception ex) {
                    return UN_KNOWN;
                }
            }
        }
        return request.getRemoteAddr();
    }
}
