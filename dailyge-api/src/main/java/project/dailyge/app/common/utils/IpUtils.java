package project.dailyge.app.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import static project.dailyge.app.constant.LogConstant.IP_HEADERS;

@Slf4j
public final class IpUtils {

    private static final String DELIMITER = ",";
    private static final String UN_KNOWN = "unknown";

    private IpUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static String extractIpAddress(final HttpServletRequest request) {
        for (final String header : IP_HEADERS) {
            final String ips = request.getHeader(header);
            if (ips != null && !ips.isEmpty() && !UN_KNOWN.equalsIgnoreCase(ips)) {
                try {
                    return ips.split(DELIMITER)[0];
                } catch (Exception ex) {
                    log.error("Error parsing IP", ex);
                    return UN_KNOWN;
                }
            }
        }
        return request.getRemoteAddr();
    }
}
