package project.dailyge.common.exception;

public final class RedisExceptionUtils {

    private RedisExceptionUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static void resolveRedisSystemException(
        final String message,
        final Runnable runnable
    ) {
        if (message.contains("NOSCRIPT")) {
            reRegisterSha1(runnable);
        }
    }

    private static void reRegisterSha1(final Runnable runnable) {
        runnable.run();
    }
}
