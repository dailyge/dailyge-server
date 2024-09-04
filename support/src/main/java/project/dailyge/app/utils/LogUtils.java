package project.dailyge.app.utils;

import com.fasterxml.uuid.Generators;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LogUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String GUEST = "{ \"userId\":null, \"role\":\"GUEST\" }";
    private static final String EMPTY_STRING = "null";
    private static final String LOG_FORMAT = "{\"server\":\"%s\", \"path\":\"%s\", \"method\":\"%s\", \"traceId\":\"%s\", "
        + "\"ip\":\"%s\", \"layer\":\"%s\", \"visitor\":\"%s\", \"time\":\"%s\", \"duration\":\"%dms\", \"context\":{\"args\":%s, \"result\":%s}}";

    private LogUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static String createLogMessage(
        final String server,
        final String path,
        final String method,
        final String traceId,
        final String ipAddress,
        final String layer,
        final String visitor,
        final LocalDateTime time,
        final long duration,
        final Object args,
        final Object result
    ) {
        final String formattedTime = time.format(DATE_TIME_FORMATTER);
        final String argsString = (args != null) ? args.toString() : EMPTY_STRING;
        final String resultString = (result != null) ? result.toString() : EMPTY_STRING;

        return String.format(
            LOG_FORMAT,
            server,
            path,
            method,
            traceId,
            ipAddress,
            layer,
            getVisitor(visitor),
            formattedTime,
            duration,
            argsString,
            resultString
        );
    }

    public static String createLogMessage(
        final String server,
        final String path,
        final String method,
        final String ipAddress,
        final String layer,
        final String visitor,
        final LocalDateTime time,
        final long duration,
        final Object args,
        final Object result
    ) {
        final String formattedTime = time.format(DATE_TIME_FORMATTER);
        final String argsString = (args != null) ? args.toString() : EMPTY_STRING;
        final String resultString = (result != null) ? result.toString() : EMPTY_STRING;

        return String.format(
            LOG_FORMAT,
            server,
            method,
            path,
            createTraceId(),
            ipAddress,
            layer,
            getVisitor(visitor),
            formattedTime,
            duration,
            argsString,
            resultString
        );
    }

    public static String createGuestLogMessage(
        final String server,
        final String path,
        final String method,
        final String traceId,
        final String ipAddress,
        final String layer,
        final LocalDateTime time,
        final long duration,
        final Object args,
        final Object result
    ) {
        final String formattedTime = time.format(DATE_TIME_FORMATTER);
        final String argsString = (args != null) ? args.toString() : EMPTY_STRING;
        final String resultString = (result != null) ? result.toString() : EMPTY_STRING;

        return String.format(
            LOG_FORMAT,
            server,
            path,
            method,
            traceId,
            ipAddress,
            layer,
            GUEST,
            formattedTime,
            duration,
            argsString,
            resultString
        );
    }

    private static String createTraceId() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public static String getGuest() {
        return GUEST;
    }

    private static String getVisitor(final String visitor) {
        if (visitor == null || visitor.isBlank()) {
            return GUEST;
        }
        return visitor;
    }
}
