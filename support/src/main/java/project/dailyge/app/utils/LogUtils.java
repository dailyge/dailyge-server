package project.dailyge.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class LogUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String NULL_STRING = "null";
    private static final String LOG_FORMAT = "{\"order\":\"%d\", \"layer\":\"%s\", \"path\":\"%s\", \"method\":\"%s\", \"traceId\":\"%s\", "
        + "\"ip\":\"%s\", \"visitor\":%s, \"time\":\"%s\", \"duration\":\"%dms\", \"context\":{\"args\":%s, \"result\":%s}, \"level\":\"%s\"}";

    private static final ObjectMapper objectMapper;
    private static final String GUEST_JSON;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            GUEST_JSON = objectMapper.writeValueAsString(new Visitor());
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private LogUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static String createLogMessage(
        final int order,
        final String layer,
        final String path,
        final String method,
        final String traceId,
        final String ipAddress,
        final LocalDateTime time,
        final long duration,
        final Object args,
        final Object result,
        final String visitor,
        final String level
    ) throws JsonProcessingException {
        final String formattedTime = time.format(DATE_TIME_FORMATTER);
        final String argsString = (args != null) ? objectMapper.writeValueAsString(args) : NULL_STRING;
        final String resultString = (result != null) ? objectMapper.writeValueAsString(result) : NULL_STRING;
        return String.format(
            LOG_FORMAT,
            order,
            layer,
            path,
            method,
            traceId,
            ipAddress,
            (visitor != null) ? visitor : GUEST_JSON,
            formattedTime,
            duration,
            argsString,
            resultString,
            level
        );
    }

    public static String createGuestLogMessage(
        final int order,
        final String layer,
        final String path,
        final String method,
        final String traceId,
        final String ipAddress,
        final LocalDateTime time,
        final long duration,
        final Object args,
        final Object result,
        final String level
    ) {
        final String formattedTime = time.format(DATE_TIME_FORMATTER);
        final String argsString = (args != null) ? args.toString() : NULL_STRING;
        final String resultString = (result != null) ? result.toString() : NULL_STRING;
        return String.format(
            LOG_FORMAT,
            order,
            layer,
            path,
            method,
            traceId,
            ipAddress,
            GUEST_JSON,
            formattedTime,
            duration,
            argsString,
            resultString,
            level
        );
    }

    public static String getVisitor(final String visitor) {
        if (visitor == null || visitor.isBlank()) {
            return GUEST_JSON;
        }
        return visitor;
    }

    @Getter
    public static class Visitor {
        private Long userId;
        private final String role = "GUEST";

        public Visitor() {
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Visitor visitor = (Visitor) obj;
            return Objects.equals(userId, visitor.userId) && Objects.equals(role, visitor.role);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, role);
        }
    }
}
