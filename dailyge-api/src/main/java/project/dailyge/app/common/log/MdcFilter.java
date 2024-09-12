package project.dailyge.app.common.log;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import static project.dailyge.app.common.auth.DailygeUser.getDailygeUser;
import static project.dailyge.app.common.utils.IpUtils.extractIpAddress;
import static project.dailyge.app.constant.LogConstant.ENTRANCE_LAYER;
import static project.dailyge.app.constant.LogConstant.IP;
import static project.dailyge.app.constant.LogConstant.LOG_ORDER;
import static project.dailyge.app.constant.LogConstant.TRACE_ID;
import static project.dailyge.app.utils.LogUtils.createGuestLogMessage;
import static project.dailyge.app.utils.LogUtils.createLogMessage;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Order(1)
@Component
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
            MDC.put(TRACE_ID, traceId);
            MDC.put(IP, userIp);
            MDC.put(LOG_ORDER, "0");

            increaseOrder();
            final int order = Integer.parseInt(MDC.get(LOG_ORDER));
            final String longMessage = createGuestLogMessage(order, ENTRANCE_LAYER, path, method, traceId, userIp, startTime, 0, null, null);
            log.info(longMessage);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            final LocalDateTime endTime = LocalDateTime.now();
            final long duration = ChronoUnit.MILLIS.between(startTime, endTime);

            increaseOrder();
            final int order = Integer.parseInt(MDC.get(LOG_ORDER));
            final DailygeUser dailygeUser = getDailygeUser(servletRequest.getAttribute(DAILYGE_USER));
            final String longMessage = createLogMessage(
                order, ENTRANCE_LAYER, path, method, traceId, userIp, endTime, duration, null, null, dailygeUser != null ? dailygeUser.toString() : null
            );
            servletRequest.removeAttribute(DAILYGE_USER);
            log.info(longMessage);
            MDC.clear();
        }
    }

    private static void increaseOrder() {
        final int order = Integer.parseInt(MDC.get(LOG_ORDER));
        MDC.put(LOG_ORDER, String.valueOf(order + 1));
    }
}
