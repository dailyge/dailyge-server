package project.dailyge.common.exception;

public final class RedisExceptionUtils {

    public static void resolveRedisSystemException(
        final String message,
        final Runnable runnable
    ) {
        if (message.contains("NOSCRIPT")) {
            runnable.run();
        }
    }
}
